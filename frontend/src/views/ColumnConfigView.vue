<template>
  <div class="space-y-4">
    <SectionCard
      title="字段列配置"
      icon="view_column"
      hint="配置数据表格的列定义"
      muted
    >
      <template #actions>
        <div class="relative">
          <span class="material-symbols-outlined absolute left-2 top-1/2 -translate-y-1/2 text-on-surface-variant text-[18px]">search</span>
          <input
            v-model="keyword"
            class="pl-8 pr-3 py-1 text-[13px] bg-white border border-outline-variant focus:border-primary outline-none w-56"
            placeholder="搜索列名 / 键…"
          />
        </div>
        <button
          @click="openEditor()"
          class="text-[12px] uppercase tracking-wide font-medium px-3 py-1 bg-primary text-on-primary rounded hover:opacity-90 transition flex items-center gap-1"
        >
          <span class="material-symbols-outlined text-[16px]">add</span>添加列
        </button>
      </template>

      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-[#F1F5F9] border-y border-outline-variant">
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-12">#</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">列显示名</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">数据键</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-24">列宽</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-28">对齐</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-20">可见</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant w-20">可排序</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right w-24">管理</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant">
            <tr
              v-for="(c, idx) in filtered"
              :key="c.id"
              class="hover:bg-surface-container-lowest transition"
            >
              <td class="px-4 py-3 text-[12px] text-outline font-mono">{{ idx + 1 }}</td>
              <td class="px-4 py-3 text-sm font-semibold text-on-background">{{ c.displayName }}</td>
              <td class="px-4 py-3">
                <code class="font-mono text-[12px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">{{ c.dataKey }}</code>
              </td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ c.width || '—' }}</td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ alignLabel(c.alignment) }}</td>
              <td class="px-4 py-3">
                <ToggleSwitch :model-value="c.visible" @update:model-value="quickUpdate(c, 'visible', $event)" />
              </td>
              <td class="px-4 py-3">
                <ToggleSwitch :model-value="c.sortable" @update:model-value="quickUpdate(c, 'sortable', $event)" />
              </td>
              <td class="px-4 py-3 text-right">
                <button @click="openEditor(c)" class="p-1 text-outline hover:text-primary transition">
                  <span class="material-symbols-outlined">edit</span>
                </button>
                <button @click="remove(c)" class="p-1 text-outline hover:text-error transition ml-1">
                  <span class="material-symbols-outlined">delete</span>
                </button>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="8" class="px-4 py-10 text-center text-on-surface-variant text-sm">暂无字段列，点击「添加列」开始配置</td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>

    <!-- 编辑器弹窗 -->
    <ModalDialog v-if="editor.open" :title="editor.data.id ? '编辑字段列' : '新增字段列'" @close="editor.open = false">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">列显示名 *</label>
          <input v-model="editor.data.displayName" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">数据键 *</label>
          <input v-model="editor.data.dataKey" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="snake_case" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">列宽 (px)</label>
          <input v-model.number="editor.data.width" type="number" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">对齐方式</label>
          <select v-model="editor.data.alignment" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
            <option v-for="a in ALIGNMENT_OPTIONS" :key="a.value" :value="a.value">{{ a.label }}</option>
          </select>
        </div>
        <div class="flex items-center gap-2">
          <ToggleSwitch :model-value="editor.data.visible" @update:model-value="editor.data.visible = $event" />
          <span class="text-sm text-on-surface">可见</span>
        </div>
        <div class="flex items-center gap-2">
          <ToggleSwitch :model-value="editor.data.sortable" @update:model-value="editor.data.sortable = $event" />
          <span class="text-sm text-on-surface">允许排序</span>
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
import ToggleSwitch from '@/components/ToggleSwitch.vue'
import { ColumnApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { ALIGNMENT_OPTIONS, type Alignment, type ColumnConfig } from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const columns = ref<ColumnConfig[]>([])
const keyword = ref('')

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return columns.value
  return columns.value.filter(
    (c) => c.displayName.toLowerCase().includes(k) || c.dataKey.toLowerCase().includes(k)
  )
})

function alignLabel(a: Alignment) {
  return ALIGNMENT_OPTIONS.find((x) => x.value === a)?.label || a
}

const blank = (): ColumnConfig => ({
  sortOrder: (columns.value.at(-1)?.sortOrder || 0) + 1,
  displayName: '',
  dataKey: '',
  width: 140,
  alignment: 'LEFT',
  visible: true,
  sortable: false
})

const editor = reactive<{ open: boolean; data: ColumnConfig }>({
  open: false,
  data: blank()
})

function openEditor(c?: ColumnConfig) {
  editor.data = c ? { ...c } : blank()
  editor.open = true
}

async function load() {
  columns.value = await ColumnApi.listByTable(tableId.value)
}

async function save() {
  const d = editor.data
  if (!d.displayName.trim() || !d.dataKey.trim()) {
    toast.error('列显示名与数据键必填')
    return
  }
  try {
    if (d.id) {
      await ColumnApi.update(tableId.value, d.id, d)
    } else {
      await ColumnApi.add(tableId.value, d)
    }
    toast.success('已保存')
    editor.open = false
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function quickUpdate(c: ColumnConfig, field: 'visible' | 'sortable', value: boolean) {
  const payload = { ...c, [field]: value }
  try {
    await ColumnApi.update(tableId.value, c.id!, payload)
    c[field] = value
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function remove(c: ColumnConfig) {
  if (!confirm(`删除字段列「${c.displayName}」？`)) return
  try {
    await ColumnApi.remove(tableId.value, c.id!)
    toast.success('已删除')
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

onMounted(load)
</script>
