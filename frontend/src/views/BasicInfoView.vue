<template>
  <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
    <div class="lg:col-span-2 space-y-6">
      <!-- 实体定义 -->
      <SectionCard title="实体定义" icon="storage" hint="定义表格核心属性">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">表格名称 *</label>
            <input
              v-model="form.tableName"
              class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none"
              placeholder="如：资产主表"
            />
          </div>
          <div>
            <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">唯一编码 *</label>
            <input
              v-model="form.tableCode"
              class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono"
              placeholder="如：T_ASSET_MAIN"
            />
          </div>
          <div>
            <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">数据源</label>
            <select v-model="form.dataSource" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
              <option v-for="d in dataSources" :key="d">{{ d }}</option>
            </select>
          </div>
          <div>
            <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">初始状态</label>
            <select v-model="form.status" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white" :disabled="isNew">
              <option value="DRAFT">草稿 DRAFT</option>
              <option value="PUBLISHED">已发布 PUBLISHED</option>
            </select>
          </div>
          <div class="md:col-span-2">
            <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">备注</label>
            <textarea
              v-model="form.description"
              rows="2"
              class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none"
              placeholder="表格用途说明…"
            />
          </div>
        </div>
      </SectionCard>

      <!-- 页面类型 -->
      <SectionCard title="页面类型" icon="category" hint="决定维护形态：表格 or 表单">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <button
            v-for="opt in pageTypeOptions"
            :key="opt.value"
            type="button"
            @click="form.pageType = opt.value"
            class="text-left p-3 border-2 rounded transition flex items-start gap-3"
            :class="form.pageType === opt.value ? 'border-primary bg-surface-container-low' : 'border-outline-variant hover:border-on-surface-variant'"
          >
            <span class="material-symbols-outlined mt-0.5" :class="form.pageType === opt.value ? 'text-primary' : 'text-outline'">{{ opt.icon }}</span>
            <div>
              <p class="text-sm font-semibold" :class="form.pageType === opt.value ? 'text-primary' : 'text-on-surface'">{{ opt.label }}</p>
              <p class="text-[12px] text-on-surface-variant">{{ opt.desc }}</p>
            </div>
          </button>
        </div>
      </SectionCard>

      <!-- 显示优化 / 布局选择 -->
      <SectionCard title="显示优化" icon="dashboard_customize" hint="选择列表展示布局">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <button
            v-for="opt in layoutOptions"
            :key="opt.value"
            type="button"
            @click="form.layoutMode = opt.value"
            class="text-left p-3 border-2 rounded transition flex items-start gap-3"
            :class="form.layoutMode === opt.value ? 'border-primary bg-surface-container-low' : 'border-outline-variant hover:border-on-surface-variant'"
          >
            <span
              class="material-symbols-outlined mt-0.5"
              :class="form.layoutMode === opt.value ? 'text-primary' : 'text-outline'"
            >{{ opt.icon }}</span>
            <div>
              <p class="text-sm font-semibold" :class="form.layoutMode === opt.value ? 'text-primary' : 'text-on-surface'">{{ opt.label }}</p>
              <p class="text-[12px] text-on-surface-variant">{{ opt.desc }}</p>
            </div>
          </button>
        </div>
      </SectionCard>
    </div>

    <!-- 侧边提示 -->
    <div class="space-y-6">
      <SectionCard title="操作" icon="task_alt">
        <button
          @click="save"
          :disabled="saving"
          class="w-full bg-primary text-on-primary py-2 font-bold text-[13px] uppercase tracking-wide rounded hover:opacity-90 transition disabled:opacity-50"
        >
          {{ saving ? '保存中…' : isNew ? '创建表格' : '保存更改' }}
        </button>
        <button
          @click="cancel"
          class="w-full mt-2 border border-outline-variant py-2 text-[13px] text-on-surface-variant rounded hover:bg-surface-container-high transition"
        >
          {{ isNew ? '放弃创建' : '取消' }}
        </button>
        <div class="mt-4 text-[11px] text-outline flex items-start gap-1.5">
          <span class="material-symbols-outlined text-[14px] mt-0.5">history</span>
          <span v-if="detail">最近更新：{{ formatTime(detail.updatedAt) }}</span>
          <span v-else>新建后将进入字段配置。</span>
        </div>
      </SectionCard>

      <div class="bg-primary p-5 text-on-primary">
        <div class="flex items-start gap-3">
          <span class="material-symbols-outlined text-[28px]">auto_awesome</span>
          <div>
            <h4 class="font-bold text-sm">配置指南</h4>
            <p class="text-[12px] opacity-80 mt-1 leading-relaxed">
              唯一编码采用 <code class="font-mono">T_</code> 前缀 + 大写下划线，创建后不建议频繁修改。
              布局模式决定「数据查询中心」如何呈现明细。
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import { TableApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { LAYOUT_MODE_OPTIONS, PAGE_TYPE_OPTIONS, type LayoutMode, type PageType, type TableConfigDetail, type TableStatus } from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const id = computed(() => String(route.params.id || ''))
const isNew = computed(() => id.value === 'new')
const detail = ref<TableConfigDetail | null>(null)
const saving = ref(false)

const dataSources = ['PostgreSQL 生产集群', '遗留 SAP 集成', '实时 IoT 枢纽', '冷存储归档']
const pageTypeOptions = PAGE_TYPE_OPTIONS

const layoutOptions = LAYOUT_MODE_OPTIONS.map((o) => ({
  value: o.value,
  label: o.label,
  desc: o.desc,
  icon: { PURE_LIST: 'list', SIDE_DRAWER: 'side_navigation', MASTER_DETAIL: 'view_list', ENHANCED_QUERY: 'filter_alt' }[o.value]
}))

const form = reactive({
  tableName: '',
  tableCode: '',
  dataSource: dataSources[0],
  pageType: 'GRID' as PageType,
  layoutMode: 'SIDE_DRAWER' as LayoutMode,
  status: 'DRAFT' as TableStatus,
  description: ''
})

async function load() {
  if (isNew.value) return
  try {
    detail.value = await TableApi.detail(id.value)
    Object.assign(form, {
      tableName: detail.value.tableName,
      tableCode: detail.value.tableCode,
      dataSource: detail.value.dataSource || dataSources[0],
      pageType: detail.value.pageType || 'GRID',
      layoutMode: detail.value.layoutMode || 'PURE_LIST',
      status: detail.value.status || 'DRAFT',
      description: detail.value.description || ''
    })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function save() {
  if (!form.tableName.trim() || !form.tableCode.trim()) {
    toast.error('表格名称与唯一编码必填')
    return
  }
  saving.value = true
  try {
    if (isNew.value) {
      const created = await TableApi.create({
        ...form,
        columns: [],
        queryConditions: [],
        buttons: [],
        formFields: [],
        subTables: []
      })
      toast.success('创建成功')
      router.replace(`/tables/${created.id}/basic`)
    } else {
      await TableApi.updateBasic(id.value, form)
      detail.value = await TableApi.detail(id.value)
      toast.success('已保存')
    }
  } catch (e: any) {
    toast.error(e.message)
  } finally {
    saving.value = false
  }
}

function cancel() {
  router.push('/')
}

function formatTime(t?: string) {
  if (!t) return '—'
  return t.replace('T', ' ').slice(0, 19)
}

onMounted(load)
</script>
