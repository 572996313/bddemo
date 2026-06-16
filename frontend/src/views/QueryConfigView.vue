<template>
  <div class="space-y-4">
    <SectionCard title="查询条件配置" icon="filter_alt" hint="定义数据查询中心的筛选条件" muted>
      <template #actions>
        <button
          @click="openEditor()"
          class="text-[12px] uppercase tracking-wide font-medium px-3 py-1 bg-primary text-on-primary rounded hover:opacity-90 transition flex items-center gap-1"
        >
          <span class="material-symbols-outlined text-[16px]">add</span>添加条件
        </button>
      </template>

      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-[#F1F5F9] border-y border-outline-variant">
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-12">#</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">查询标签</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">字段名</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">控件类型</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">匹配方式</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">默认值</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right w-24">管理</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant">
            <tr v-for="(q, idx) in conditions" :key="q.id" class="hover:bg-surface-container-lowest transition">
              <td class="px-4 py-3 text-[12px] text-outline font-mono">{{ String(idx + 1).padStart(2, '0') }}</td>
              <td class="px-4 py-3 text-sm font-semibold text-on-background">{{ q.label }}</td>
              <td class="px-4 py-3"><code class="font-mono text-[12px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">{{ q.fieldKey }}</code></td>
              <td class="px-4 py-3">
                <span class="text-[12px] px-2 py-0.5 rounded-full bg-surface-container-high text-on-surface-variant">{{ ctrlLabel(q.controlType) }}</span>
              </td>
              <td class="px-4 py-3 text-[12px] text-on-surface-variant">{{ matchLabel(q.matchMode) }}</td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ q.defaultValue || '—' }}</td>
              <td class="px-4 py-3 text-right">
                <button @click="openEditor(q)" class="p-1 text-outline hover:text-primary transition"><span class="material-symbols-outlined">edit</span></button>
                <button @click="remove(q)" class="p-1 text-outline hover:text-error transition ml-1"><span class="material-symbols-outlined">delete</span></button>
              </td>
            </tr>
            <tr v-if="!conditions.length">
              <td colspan="7" class="px-4 py-10 text-center text-on-surface-variant text-sm">暂无查询条件</td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>

    <ModalDialog v-if="editor.open" :title="editor.data.id ? '编辑查询条件' : '新增查询条件'" @close="editor.open = false">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">查询标签 *</label>
          <input v-model="editor.data.label" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">字段名 *</label>
          <input v-model="editor.data.fieldKey" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">控件类型</label>
          <select v-model="editor.data.controlType" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
            <option v-for="c in CONTROL_TYPE_OPTIONS" :key="c.value" :value="c.value">{{ c.label }}</option>
          </select>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">匹配方式</label>
          <select v-model="editor.data.matchMode" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
            <option v-for="m in MATCH_MODE_OPTIONS" :key="m.value" :value="m.value">{{ m.label }}</option>
          </select>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">默认值</label>
          <input v-model="editor.data.defaultValue" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">校验规则</label>
          <input v-model="editor.data.validationRule" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" placeholder="可选" />
        </div>
      </div>
      <template #footer>
        <button @click="editor.open = false" class="px-4 py-2 text-[13px] border border-outline-variant rounded hover:bg-surface-container-high transition">取消</button>
        <button @click="save" class="px-4 py-2 text-[13px] bg-primary text-on-primary rounded hover:opacity-90 transition">保存</button>
      </template>
    </ModalDialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import ModalDialog from '@/components/ModalDialog.vue'
import { QueryApi } from '@/api'
import { useToast } from '@/composables/useToast'
import {
  CONTROL_TYPE_OPTIONS,
  MATCH_MODE_OPTIONS,
  type ControlType,
  type MatchMode,
  type QueryCondition
} from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const conditions = ref<QueryCondition[]>([])

function ctrlLabel(c: ControlType) {
  return CONTROL_TYPE_OPTIONS.find((x) => x.value === c)?.label || c
}
function matchLabel(m: MatchMode) {
  return MATCH_MODE_OPTIONS.find((x) => x.value === m)?.label || m
}

const blank = (): QueryCondition => ({
  sortOrder: (conditions.value.at(-1)?.sortOrder || 0) + 1,
  label: '',
  fieldKey: '',
  controlType: 'INPUT',
  matchMode: 'FUZZY',
  defaultValue: '',
  validationRule: ''
})

const editor = reactive<{ open: boolean; data: QueryCondition }>({ open: false, data: blank() })

function openEditor(q?: QueryCondition) {
  editor.data = q ? { ...q } : blank()
  editor.open = true
}

async function load() {
  conditions.value = await QueryApi.listByTable(tableId.value)
}

async function save() {
  const d = editor.data
  if (!d.label.trim() || !d.fieldKey.trim()) {
    toast.error('查询标签与字段名必填')
    return
  }
  try {
    if (d.id) await QueryApi.update(tableId.value, d.id, d)
    else await QueryApi.add(tableId.value, d)
    toast.success('已保存')
    editor.open = false
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function remove(q: QueryCondition) {
  if (!confirm(`删除查询条件「${q.label}」？`)) return
  try {
    await QueryApi.remove(tableId.value, q.id!)
    toast.success('已删除')
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

onMounted(load)
</script>
