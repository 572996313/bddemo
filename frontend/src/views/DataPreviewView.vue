<template>
  <div class="space-y-4">
    <!-- 查询条件条 -->
    <SectionCard :title="`${data?.tableName || '加载中'} · 查询面板`" icon="search" hint="查询条件由元数据驱动" muted>
      <div class="flex flex-wrap items-end gap-4">
        <div v-for="q in conditions" :key="q.id" class="flex flex-col">
          <label class="text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">{{ q.label }}</label>
          <input
            v-if="q.controlType === 'INPUT'"
            v-model="filterValues[q.fieldKey]"
            class="text-sm border border-outline-variant px-3 py-1.5 focus:border-primary outline-none w-48"
            :placeholder="matchLabel(q.matchMode)"
          />
          <select
            v-else-if="q.controlType === 'DROPDOWN'"
            v-model="filterValues[q.fieldKey]"
            class="text-sm border border-outline-variant px-3 py-1.5 outline-none focus:border-primary bg-white w-48"
          >
            <option value="">全部</option>
            <option v-for="s in statusOpts" :key="s">{{ s }}</option>
          </select>
          <div v-else-if="q.controlType === 'DATE_RANGE'" class="flex items-center gap-1">
            <input type="date" v-model="filterValues[q.fieldKey + '_from']" class="text-sm border border-outline-variant px-2 py-1.5 outline-none focus:border-primary" />
            <span class="text-outline">~</span>
            <input type="date" v-model="filterValues[q.fieldKey + '_to']" class="text-sm border border-outline-variant px-2 py-1.5 outline-none focus:border-primary" />
          </div>
          <input v-else v-model="filterValues[q.fieldKey]" class="text-sm border border-outline-variant px-3 py-1.5 focus:border-primary outline-none w-48" :placeholder="ctrlLabel(q.controlType)" />
        </div>
        <div class="flex gap-2 ml-auto">
          <button @click="resetFilter" class="px-3 py-1.5 text-[13px] border border-outline-variant text-on-surface-variant rounded hover:bg-surface-container-high transition flex items-center gap-1">
            <span class="material-symbols-outlined text-[16px]">restart_alt</span>重置
          </button>
          <button @click="runFilter" class="px-3 py-1.5 text-[13px] bg-secondary text-on-secondary rounded hover:opacity-90 transition flex items-center gap-1">
            <span class="material-symbols-outlined text-[16px]">search</span>查询
          </button>
        </div>
      </div>
    </SectionCard>

    <!-- 数据表格 -->
    <SectionCard icon="table_view" muted>
      <template #actions>
        <div class="flex gap-2">
          <button
            v-for="btn in headerButtons"
            :key="btn.id"
            @click="toast.info(`触发：${btn.name}`)"
            class="px-3 py-1 text-[12px] font-medium flex items-center gap-1"
            :class="headerBtnClass(btn.buttonStyle)"
          >
            <span class="material-symbols-outlined text-[16px]">{{ btn.icon || 'extension' }}</span>
            {{ btn.name }}
          </button>
        </div>
      </template>

      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-[#F1F5F9] border-y border-outline-variant">
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-10">#</th>
              <th
                v-for="col in data?.columns"
                :key="col.dataKey"
                class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant"
                :style="{ width: col.width ? col.width + 'px' : undefined, textAlign: col.alignment.toLowerCase() as any }"
              >
                {{ col.displayName }}
                <span v-if="col.sortable" class="material-symbols-outlined text-[12px] align-middle text-outline">unfold_more</span>
              </th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant zebra">
            <tr
              v-for="(row, idx) in visibleRows"
              :key="idx"
              class="hover:bg-surface-container-lowest transition cursor-pointer"
              :class="selectedIdx === idx ? 'bg-secondary-container/30' : ''"
              @click="selectedIdx = idx"
            >
              <td class="px-4 py-3 text-[12px] text-outline font-mono">{{ idx + 1 }}</td>
              <td
                v-for="col in data?.columns"
                :key="col.dataKey"
                class="px-4 py-3 text-[13px]"
                :class="alignClass(col.alignment)"
              >
                <span v-if="isStatusKey(col.dataKey)" class="px-2 py-0.5 rounded-full text-[11px] font-medium" :class="statusClass(row[col.dataKey])">{{ statusText(row[col.dataKey]) }}</span>
                <span v-else-if="col.dataKey.includes('amount')" class="font-mono text-on-surface">¥{{ row[col.dataKey] }}</span>
                <span v-else :class="col.dataKey.includes('id') ? 'font-mono text-secondary' : 'text-on-surface-variant'">{{ row[col.dataKey] }}</span>
              </td>
              <td class="px-4 py-3 text-right">
                <div class="inline-flex gap-1">
                  <button
                    v-for="btn in rowButtons"
                    :key="btn.id"
                    @click.stop="toast.info(`行内：${btn.name}`)"
                    class="text-[11px] px-1.5 border"
                    :class="rowBtnClass(btn.buttonStyle)"
                  >{{ btn.name }}</button>
                </div>
              </td>
            </tr>
            <tr v-if="!visibleRows.length">
              <td :colspan="(data?.columns?.length || 0) + 2" class="px-4 py-10 text-center text-on-surface-variant text-sm">没有匹配的数据</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="flex items-center justify-between px-2 py-3 border-t border-outline-variant text-[12px] text-on-surface-variant">
        <span>共 {{ visibleRows.length }} 条 · 模拟数据（由列元数据生成）</span>
        <span>布局模式：{{ layoutLabel }}</span>
      </div>
    </SectionCard>

    <!-- 主从表联动：选中行详情 + 明细子表 -->
    <div v-if="isMasterDetail && selectedRow" class="grid grid-cols-1 lg:grid-cols-3 gap-4">
      <SectionCard title="选中行 · 基本信息" icon="description" class="lg:col-span-1">
        <div class="space-y-2">
          <div v-for="col in (data?.columns || []).slice(0, 5)" :key="col.dataKey" class="flex justify-between text-[13px] border-b border-dashed border-outline-variant pb-1">
            <span class="text-on-surface-variant">{{ col.displayName }}</span>
            <span class="font-medium text-on-surface text-right">{{ selectedRow[col.dataKey] }}</span>
          </div>
        </div>
      </SectionCard>
      <SectionCard title="明细 · 物料清单" icon="inventory_2" class="lg:col-span-2">
        <div class="overflow-x-auto">
          <table class="w-full text-left border-collapse">
            <thead>
              <tr class="bg-[#F1F5F9] border-y border-outline-variant">
                <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">物料名称</th>
                <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">规格</th>
                <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-right">数量</th>
                <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-right">单价</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-outline-variant zebra">
              <tr v-for="(it, i) in detailItems" :key="i">
                <td class="px-3 py-2 text-[13px] text-on-surface">{{ it.name }}</td>
                <td class="px-3 py-2 text-[13px] text-on-surface-variant">{{ it.spec }}</td>
                <td class="px-3 py-2 text-[13px] text-right font-mono">{{ it.qty }}</td>
                <td class="px-3 py-2 text-[13px] text-right font-mono">¥{{ it.price }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </SectionCard>
    </div>

    <!-- 侧边抽屉：行详情 -->
    <Teleport v-if="isSideDrawer && selectedRow" to="body">
      <div class="fixed inset-0 z-[80]">
        <div class="absolute inset-0 bg-black/30" @click="selectedIdx = -1"></div>
        <div class="absolute right-0 top-0 h-full w-[560px] bg-white shadow-2xl flex flex-col">
          <header class="px-5 py-3 border-b border-outline-variant flex items-center justify-between bg-[#F8FAFC]">
            <div class="flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">panel_right</span>
              <h3 class="text-[13px] font-bold text-on-surface">详情 · {{ selectedRow[mainKey] }}</h3>
            </div>
            <button @click="selectedIdx = -1" class="text-outline hover:text-on-surface"><span class="material-symbols-outlined">close</span></button>
          </header>
          <div class="flex-1 overflow-y-auto p-5 space-y-5">
            <div>
              <h4 class="text-[11px] font-bold uppercase tracking-wide text-on-surface-variant mb-2">基本信息</h4>
              <div class="grid grid-cols-2 gap-3">
                <div v-for="col in (data?.columns || [])" :key="col.dataKey" class="border border-outline-variant rounded px-3 py-2">
                  <p class="text-[10px] uppercase text-outline">{{ col.displayName }}</p>
                  <p class="text-[13px] font-medium text-on-surface">{{ selectedRow[col.dataKey] }}</p>
                </div>
              </div>
            </div>
            <div>
              <h4 class="text-[11px] font-bold uppercase tracking-wide text-on-surface-variant mb-2">操作日志</h4>
              <div class="space-y-2">
                <div v-for="(log, i) in opLogs" :key="i" class="flex gap-2 text-[12px]">
                  <span class="w-2 h-2 rounded-full bg-secondary mt-1.5"></span>
                  <div><span class="font-medium text-on-surface">{{ log.text }}</span><span class="text-outline block">{{ log.time }}</span></div>
                </div>
              </div>
            </div>
          </div>
          <footer class="px-5 py-3 border-t border-outline-variant flex justify-end gap-2 bg-[#F8FAFC]">
            <button @click="selectedIdx = -1" class="px-4 py-1.5 text-[13px] border border-outline-variant rounded">关闭</button>
            <button @click="toast.info('提交变更')" class="px-4 py-1.5 text-[13px] bg-primary text-on-primary rounded">提交变更</button>
          </footer>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import { PreviewApi, TableApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { CONTROL_TYPE_OPTIONS, MATCH_MODE_OPTIONS, type ButtonStyle, type QueryCondition, type TableButton } from '@/types'

const route = useRoute()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const data = ref<any>(null)
const conditions = ref<QueryCondition[]>([])
const buttons = ref<TableButton[]>([])
const filterValues = reactive<Record<string, any>>({})
const selectedIdx = ref(0)

const statusOpts = ['进行中', '已完成', '待审核', '草稿', 'pending', 'processing', 'shipped', 'delivered']

const headerButtons = computed(() => buttons.value.filter((b) => b.enabled && b.position === 'HEADER'))
const rowButtons = computed(() => buttons.value.filter((b) => b.enabled && b.position === 'ROW'))

const visibleRows = computed(() => {
  if (!data.value?.rows) return []
  const rows = data.value.rows as Record<string, any>[]
  // 仅对 INPUT 类条件做客户端模糊过滤
  return rows.filter((row) => {
    for (const q of conditions.value) {
      const v = filterValues[q.fieldKey]
      if (v && typeof v === 'string' && v.trim()) {
        const cell = String(row[q.fieldKey] ?? '')
        if (!cell.toLowerCase().includes(v.toLowerCase())) return false
      }
    }
    return true
  })
})

const isMasterDetail = computed(() => data.value?.layoutMode === 'MASTER_DETAIL')
const isSideDrawer = computed(() => data.value?.layoutMode === 'SIDE_DRAWER')
const layoutLabel = computed(() => {
  const m: Record<string, string> = {
    PURE_LIST: '纯列表', SIDE_DRAWER: '侧边抽屉', MASTER_DETAIL: '主从联动', ENHANCED_QUERY: '增强查询'
  }
  return m[data.value?.layoutMode] || data.value?.layoutMode
})
const mainKey = computed(() => (data.value?.columns?.[0]?.dataKey) || 'id')
const selectedRow = computed(() => (selectedIdx.value >= 0 ? visibleRows.value[selectedIdx.value] : null))

const detailItems = computed(() => [
  { name: '工业控制器 A-12', spec: 'PLC-V2-800', qty: 50, price: 12500 },
  { name: '高精度光纤传感器', spec: 'FX-01-Pro', qty: 120, price: 480 },
  { name: '耐高温密封组件', spec: 'HT-300C', qty: 80, price: 220 },
  { name: '变频器 VFD-300', spec: 'VFD-30kW', qty: 12, price: 8600 }
])
const opLogs = computed(() => [
  { text: '订单审核通过', time: '2023-11-21 14:30 · 管理员' },
  { text: '物料详情更新', time: '2023-11-21 11:08 · 张伟' },
  { text: '订单创建成功', time: '2023-11-20 09:12 · 系统' }
])

function ctrlLabel(c: any) {
  return CONTROL_TYPE_OPTIONS.find((x) => x.value === c)?.label || c
}
function matchLabel(m: any) {
  return MATCH_MODE_OPTIONS.find((x) => x.value === m)?.label || m
}

function isStatusKey(k: string) {
  return k.toLowerCase().includes('status')
}
function statusClass(v: any) {
  const s = String(v)
  if (/done|完成|delivered|已完成/i.test(s)) return 'bg-emerald-100 text-emerald-800'
  if (/pending|审核|draft|草稿|待/i.test(s)) return 'bg-amber-100 text-amber-800'
  if (/process|shipped|进行|processing/i.test(s)) return 'bg-blue-100 text-blue-800'
  return 'bg-surface-container text-on-surface-variant'
}
function statusText(v: any) {
  return Array.isArray(v) ? v.join('/') : v
}
function alignClass(a: string) {
  return { LEFT: 'text-left', CENTER: 'text-center', RIGHT: 'text-right' }[a] || 'text-left'
}
function headerBtnClass(s: ButtonStyle) {
  return {
    PRIMARY: 'bg-primary text-on-primary',
    GHOST: 'border border-outline-variant text-on-surface-variant',
    DANGER: 'border-2 border-error text-error',
    LINK: 'text-secondary hover:underline'
  }[s]
}
function rowBtnClass(s: ButtonStyle) {
  return {
    PRIMARY: 'border-primary text-primary',
    GHOST: 'border-outline-variant text-on-surface-variant',
    DANGER: 'border-error text-error',
    LINK: 'border-transparent text-primary'
  }[s]
}

function resetFilter() {
  for (const k of Object.keys(filterValues)) filterValues[k] = ''
}
function runFilter() {
  toast.success(`已应用筛选，匹配 ${visibleRows.value.length} 条`)
}

async function load() {
  const [detail, preview] = await Promise.all([TableApi.detail(tableId.value), PreviewApi.preview(tableId.value)])
  data.value = preview
  conditions.value = detail.queryConditions
  buttons.value = detail.buttons
  // 抽屉布局默认收起，主从布局默认选中首行
  selectedIdx.value = preview.layoutMode === 'SIDE_DRAWER' ? -1 : 0
}

onMounted(load)
</script>
