<template>
  <AppShell>
    <template #topbar-left>
      <div class="flex items-center gap-3">
        <button @click="$router.push('/')" class="text-on-surface-variant hover:text-primary">
          <span class="material-symbols-outlined">arrow_back</span>
        </button>
        <h2 class="text-base font-bold text-primary">
          {{ detail?.tableName || '加载中…' }}
        </h2>
        <code v-if="detail" class="font-mono text-[12px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">
          {{ detail.tableCode }}
        </code>
        <span
          v-if="detail"
          class="px-2 py-0.5 rounded-full text-[11px] font-medium"
          :class="detail.status === 'PUBLISHED' ? 'bg-emerald-100 text-emerald-800' : 'bg-amber-100 text-amber-800'"
        >
          {{ detail.status === 'PUBLISHED' ? '已发布' : '草稿' }}
        </span>
      </div>
    </template>

    <template #topbar-right>
      <button
        v-if="detail"
        @click="toggleStatus"
        class="text-[12px] uppercase tracking-wide font-medium px-3 py-1 border border-outline-variant text-on-surface-variant rounded hover:bg-surface-container-high transition flex items-center gap-1"
      >
        <span class="material-symbols-outlined text-[16px]">{{ detail.status === 'PUBLISHED' ? 'unpublished' : 'publish' }}</span>
        {{ detail.status === 'PUBLISHED' ? '转为草稿' : '发布' }}
      </button>
    </template>

    <!-- Tab 导航 -->
    <div class="bg-white border border-outline-variant mb-4">
      <div class="flex border-b border-outline-variant">
        <RouterLink
          v-for="tab in tabs"
          :key="tab.name"
          :to="tab.to"
          class="px-5 py-3 text-[13px] font-semibold flex items-center gap-2 border-b-2 -mb-px transition-colors"
          active-class="!border-primary !text-primary"
          :class="route.name === tab.name ? 'border-primary text-primary' : 'border-transparent text-on-surface-variant hover:text-on-surface'"
        >
          <span class="material-symbols-outlined text-[18px]">{{ tab.icon }}</span>
          {{ tab.label }}
          <span v-if="tab.count !== undefined" class="text-[11px] bg-surface-container px-1.5 rounded-full">{{ tab.count }}</span>
        </RouterLink>
      </div>
    </div>

    <RouterView />
  </AppShell>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppShell from '@/components/AppShell.vue'
import { TableApi } from '@/api'
import { useToast } from '@/composables/useToast'
import type { TableConfigDetail } from '@/types'

const props = defineProps<{ id: string }>()
const route = useRoute()
const router = useRouter()
const toast = useToast()
const detail = ref<TableConfigDetail | null>(null)

async function load() {
  if (props.id === 'new') return
  try {
    detail.value = await TableApi.detail(props.id)
  } catch (e: any) {
    toast.error(e.message)
  }
}

const tabs = computed(() => {
  const isForm = detail.value?.pageType === 'FORM'
  const base = {
    name: 'table-basic',
    label: '基础信息',
    icon: 'info',
    to: `/tables/${props.id}/basic`
  }
  if (isForm) {
    return [
      base,
      {
        name: 'table-fields',
        label: '字段配置',
        icon: 'list_alt',
        to: `/tables/${props.id}/fields`,
        count: detail.value?.formFields.length
      },
      {
        name: 'table-subtables',
        label: '子表映射',
        icon: 'table_rows',
        to: `/tables/${props.id}/subtables`,
        count: detail.value?.subTables.length
      },
      {
        name: 'table-buttons',
        label: '表单按钮',
        icon: 'smart_button',
        to: `/tables/${props.id}/buttons`,
        count: detail.value?.buttons.length
      },
      { name: 'table-form-preview', label: '表单预览', icon: 'visibility', to: `/tables/${props.id}/form-preview` }
    ]
  }
  return [
    base,
    {
      name: 'table-columns',
      label: '字段配置',
      icon: 'view_column',
      to: `/tables/${props.id}/columns`,
      count: detail.value?.columns.length
    },
    {
      name: 'table-queries',
      label: '查询条件',
      icon: 'filter_alt',
      to: `/tables/${props.id}/queries`,
      count: detail.value?.queryConditions.length
    },
    {
      name: 'table-buttons',
      label: '按钮功能',
      icon: 'smart_button',
      to: `/tables/${props.id}/buttons`,
      count: detail.value?.buttons.length
    },
    { name: 'table-preview', label: '数据查询中心', icon: 'table_view', to: `/tables/${props.id}/preview` }
  ]
})

async function toggleStatus() {
  if (!detail.value) return
  try {
    await TableApi.updateStatus(props.id, detail.value.status === 'PUBLISHED' ? 'DRAFT' : 'PUBLISHED')
    detail.value = await TableApi.detail(props.id)
    toast.success('状态已更新')
  } catch (e: any) {
    toast.error(e.message)
  }
}

// 当从子页面（字段/查询/按钮）保存后，通过路由 query 的 reload 信号刷新计数
watch(
  () => route.query.reload,
  (v) => {
    if (v) load()
  }
)

onMounted(load)
</script>
