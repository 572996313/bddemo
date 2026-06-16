<template>
  <AppShell title="表格配置总览">
    <template #topbar-right>
      <button
        @click="goNew"
        class="text-[12px] uppercase tracking-wide font-medium px-4 py-1 bg-primary text-on-primary rounded hover:opacity-90 transition flex items-center gap-1"
      >
        <span class="material-symbols-outlined text-[18px]">add</span>新建表格
      </button>
    </template>

    <!-- 类型筛选 -->
    <div class="flex items-center gap-1 mb-5 border-b border-outline-variant">
      <button
        v-for="t in typeTabs"
        :key="t.value"
        @click="typeFilter = t.value"
        class="px-4 py-2.5 text-[13px] font-semibold border-b-2 -mb-px transition-colors flex items-center gap-1.5"
        :class="typeFilter === t.value ? 'border-primary text-primary' : 'border-transparent text-on-surface-variant hover:text-on-surface'"
      >
        <span class="material-symbols-outlined text-[18px]">{{ t.icon }}</span>
        {{ t.label }}
        <span class="text-[11px] bg-surface-container px-1.5 rounded-full">{{ countByType(t.value) }}</span>
      </button>
    </div>

    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">表格总数</p>
        <div class="flex items-end justify-between mt-2">
          <span class="text-4xl font-extrabold text-primary leading-none">{{ tables.length }}</span>
          <span class="text-[12px] text-emerald-600 flex items-center gap-1">
            <span class="material-symbols-outlined text-[16px]">arrow_upward</span>元数据驱动
          </span>
        </div>
      </div>
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">已发布</p>
        <div class="flex items-end justify-between mt-2">
          <span class="text-4xl font-extrabold text-primary leading-none">{{ published }}</span>
          <span class="text-[12px] text-on-surface-variant">PUBLISHED</span>
        </div>
      </div>
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">草稿</p>
        <div class="flex items-end justify-between mt-2">
          <span class="text-4xl font-extrabold text-primary leading-none">{{ draft }}</span>
          <span class="text-[12px] text-on-surface-variant">DRAFT</span>
        </div>
      </div>
      <button
        @click="goNew"
        class="bg-white border border-outline-variant p-4 flex flex-col justify-center items-center cursor-pointer hover:bg-surface-container-low transition group"
      >
        <div class="w-10 h-10 bg-primary-container text-on-primary-container rounded-full flex items-center justify-center group-hover:scale-110 transition">
          <span class="material-symbols-outlined">add</span>
        </div>
        <span class="text-[12px] font-bold text-primary mt-1">添加表格配置</span>
      </button>
    </div>

    <!-- 表格列表 -->
    <div class="bg-white border border-outline-variant">
      <div class="px-4 py-2 bg-[#F8FAFC] border-b border-outline-variant flex items-center justify-between">
        <span class="text-[12px] font-bold uppercase text-on-surface-variant tracking-widest">已定义的表格</span>
        <div class="relative">
          <span class="material-symbols-outlined absolute left-2 top-1/2 -translate-y-1/2 text-on-surface-variant text-[18px]">search</span>
          <input
            v-model="keyword"
            class="pl-8 pr-3 py-1 text-[13px] bg-white border border-outline-variant focus:border-primary outline-none w-64"
            placeholder="搜索表格名称 / 编码..."
          />
        </div>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse zebra">
          <thead>
            <tr class="bg-[#F1F5F9] border-b border-outline-variant">
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">表格名称</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">唯一编码</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">数据源</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">类型</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">配置项</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">状态</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant">
            <tr
              v-for="t in filtered"
              :key="t.id"
              class="hover:bg-surface-container-lowest transition cursor-pointer"
              @click="enter(t.id)"
            >
              <td class="px-4 py-3">
                <div class="flex flex-col">
                  <span class="text-sm font-semibold text-on-background">{{ t.tableName }}</span>
                  <span class="text-[12px] text-outline">{{ t.description }}</span>
                </div>
              </td>
              <td class="px-4 py-3">
                <code class="font-mono text-[12px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">{{ t.tableCode }}</code>
              </td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ t.dataSource || '—' }}</td>
              <td class="px-4 py-3">
                <span
                  class="px-2 py-0.5 rounded-full text-[11px] font-medium"
                  :class="t.pageType === 'FORM' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'"
                >{{ t.pageType === 'FORM' ? '表单' : '表格' }}</span>
              </td>
              <td class="px-4 py-3">
                <div class="flex gap-1 text-[11px]">
                  <template v-if="t.pageType === 'FORM'">
                    <span class="px-1.5 py-0.5 bg-surface-container rounded text-on-surface-variant">字段 {{ t.formFieldCount }}</span>
                    <span class="px-1.5 py-0.5 bg-surface-container rounded text-on-surface-variant">子表 {{ t.subTableCount }}</span>
                  </template>
                  <template v-else>
                    <span class="px-1.5 py-0.5 bg-surface-container rounded text-on-surface-variant">列 {{ t.columnCount }}</span>
                    <span class="px-1.5 py-0.5 bg-surface-container rounded text-on-surface-variant">查 {{ t.queryConditionCount }}</span>
                  </template>
                  <span class="px-1.5 py-0.5 bg-surface-container rounded text-on-surface-variant">钮 {{ t.buttonCount }}</span>
                </div>
              </td>
              <td class="px-4 py-3">
                <span
                  class="px-2 py-0.5 rounded-full text-[11px] font-medium"
                  :class="t.status === 'PUBLISHED' ? 'bg-emerald-100 text-emerald-800' : 'bg-amber-100 text-amber-800'"
                >
                  {{ t.status === 'PUBLISHED' ? '已发布' : '草稿' }}
                </span>
              </td>
              <td class="px-4 py-3 text-right" @click.stop>
                <button
                  @click="enter(t.id)"
                  class="p-1 text-outline hover:text-primary transition"
                  title="进入维护"
                >
                  <span class="material-symbols-outlined">edit</span>
                </button>
                <button
                  @click="toggleStatus(t)"
                  class="p-1 text-outline hover:text-secondary transition ml-1"
                  :title="t.status === 'PUBLISHED' ? '转为草稿' : '发布'"
                >
                  <span class="material-symbols-outlined">{{ t.status === 'PUBLISHED' ? 'unpublished' : 'publish' }}</span>
                </button>
                <button
                  @click="confirmRemove(t)"
                  class="p-1 text-outline hover:text-error transition ml-1"
                  title="删除"
                >
                  <span class="material-symbols-outlined">delete</span>
                </button>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="7" class="px-4 py-10 text-center text-on-surface-variant text-sm">
                没有匹配的表格配置
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </AppShell>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import AppShell from '@/components/AppShell.vue'
import { TableApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { type TableConfigSummary } from '@/types'

const router = useRouter()
const toast = useToast()
const tables = ref<TableConfigSummary[]>([])
const keyword = ref('')
const typeFilter = ref<'ALL' | 'GRID' | 'FORM'>('ALL')

const typeTabs = [
  { value: 'ALL' as const, label: '全部', icon: 'apps' },
  { value: 'GRID' as const, label: '表格 / 列表', icon: 'table_view' },
  { value: 'FORM' as const, label: '表单', icon: 'edit_document' }
]

function countByType(t: 'ALL' | 'GRID' | 'FORM') {
  if (t === 'ALL') return tables.value.length
  return tables.value.filter((x) => x.pageType === t).length
}

const published = computed(() => tables.value.filter((t) => t.status === 'PUBLISHED').length)
const draft = computed(() => tables.value.filter((t) => t.status === 'DRAFT').length)
const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  return tables.value.filter((t) => {
    if (typeFilter.value !== 'ALL' && t.pageType !== typeFilter.value) return false
    if (!k) return true
    return t.tableName.toLowerCase().includes(k) || t.tableCode.toLowerCase().includes(k)
  })
})

async function load() {
  try {
    tables.value = await TableApi.list()
  } catch (e: any) {
    toast.error(e.message || '加载失败')
  }
}

function enter(id: number) {
  router.push(`/tables/${id}/basic`)
}

function goNew() {
  router.push('/tables/new')
}

async function toggleStatus(t: TableConfigSummary) {
  try {
    await TableApi.updateStatus(t.id, t.status === 'PUBLISHED' ? 'DRAFT' : 'PUBLISHED')
    toast.success('状态已更新')
    await load()
  } catch (e: any) {
    toast.error(e.message)
  }
}

function confirmRemove(t: TableConfigSummary) {
  if (!confirm(`确认删除表格「${t.tableName}」？其下字段/查询/按钮配置将一并删除。`)) return
  TableApi.remove(t.id)
    .then(() => {
      toast.success('已删除')
      load()
    })
    .catch((e: any) => toast.error(e.message))
}

onMounted(load)
</script>
