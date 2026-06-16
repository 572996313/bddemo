<template>
  <div class="space-y-4">
    <SectionCard title="子表映射配置" icon="table_rows" hint="配置表单内嵌的明细子表 / 主从关联" muted>
      <template #actions>
        <button
          @click="openEditor()"
          class="text-[12px] uppercase tracking-wide font-medium px-3 py-1 bg-primary text-on-primary rounded hover:opacity-90 transition flex items-center gap-1"
        >
          <span class="material-symbols-outlined text-[16px]">add</span>添加子表
        </button>
      </template>

      <div v-if="!subtables.length" class="px-4 py-12 text-center text-on-surface-variant text-sm">
        暂无子表映射，点击「添加子表」配置明细
      </div>

      <div v-for="s in subtables" :key="s.id" class="border border-outline-variant rounded p-4 mb-3 last:mb-0">
        <div class="flex items-start justify-between">
          <div>
            <div class="flex items-center gap-2">
              <span class="material-symbols-outlined text-secondary">table_rows</span>
              <h4 class="text-sm font-bold text-on-surface">{{ s.subTableName }}</h4>
              <code class="font-mono text-[11px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">{{ s.subTableCode }}</code>
            </div>
            <div class="flex flex-wrap gap-3 mt-2 text-[12px] text-on-surface-variant">
              <span><span class="text-outline">外键字段：</span>{{ s.relationField || '—' }}</span>
              <span><span class="text-outline">表高：</span>{{ heightLabel(s.heightMode) }}<template v-if="s.fixedHeight"> / {{ s.fixedHeight }}px</template></span>
              <span><span class="text-outline">列数：</span>{{ parseCols(s.columnsJson).length }}</span>
            </div>
            <div class="flex gap-1.5 mt-2">
              <Tag v-if="s.showHeader">表头</Tag>
              <Tag v-if="s.allowAddRow">可增行</Tag>
              <Tag v-if="s.allowBatchDelete">可批量删</Tag>
            </div>
          </div>
          <div class="flex gap-1">
            <button @click="openEditor(s)" class="p-1 text-outline hover:text-primary transition"><span class="material-symbols-outlined">edit</span></button>
            <button @click="remove(s)" class="p-1 text-outline hover:text-error transition"><span class="material-symbols-outlined">delete</span></button>
          </div>
        </div>
      </div>
    </SectionCard>

    <!-- 编辑器 -->
    <ModalDialog v-if="editor.open" :title="editor.data.id ? '编辑子表映射' : '新增子表映射'" @close="editor.open = false">
      <!-- 数据关联 -->
      <h5 class="text-[12px] font-bold uppercase tracking-wide text-on-surface-variant mb-3 flex items-center gap-1">
        <span class="material-symbols-outlined text-[16px]">link</span>数据关联配置
      </h5>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-5">
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">子表名称 *</label>
          <input v-model="editor.data.subTableName" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">关联表格编码 *</label>
          <input v-model="editor.data.subTableCode" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="TB_ORDER_ITEMS_001" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">外键关联字段</label>
          <input v-model="editor.data.relationField" list="fk-list" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="order_id" />
          <datalist id="fk-list"><option value="order_id" /><option value="parent_guid" /><option value="ref_document_id" /></datalist>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">表高模式</label>
          <div class="flex gap-2">
            <button type="button" @click="setHeight('AUTO')" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.heightMode === 'AUTO' ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">自适应</button>
            <button type="button" @click="setHeight('FIXED')" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.heightMode === 'FIXED' ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">固定</button>
          </div>
        </div>
        <div v-if="editor.data.heightMode === 'FIXED'">
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">固定高度 (px)</label>
          <input v-model.number="editor.data.fixedHeight" type="number" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div class="md:col-span-2">
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">默认过滤条件 (SQL Where)</label>
          <textarea v-model="editor.data.filterCondition" rows="2" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="is_deleted = 0"></textarea>
        </div>
      </div>

      <!-- 显示规则 -->
      <h5 class="text-[12px] font-bold uppercase tracking-wide text-on-surface-variant mb-3 flex items-center gap-1">
        <span class="material-symbols-outlined text-[16px]">visibility</span>显示规则
      </h5>
      <div class="flex flex-wrap gap-4 mb-5">
        <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.showHeader" /> 显示表头</label>
        <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.allowAddRow" /> 允许新增行</label>
        <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.allowBatchDelete" /> 允许批量删除</label>
      </div>

      <!-- 字段权限矩阵 -->
      <h5 class="text-[12px] font-bold uppercase tracking-wide text-on-surface-variant mb-3 flex items-center gap-1">
        <span class="material-symbols-outlined text-[16px]">tune</span>字段权限配置
      </h5>
      <table class="w-full text-left border-collapse mb-3">
        <thead>
          <tr class="bg-[#F1F5F9] border-y border-outline-variant">
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">字段名</th>
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">Key</th>
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-center">显示</th>
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-center">可编辑</th>
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-center">必填</th>
            <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-right w-10"></th>
          </tr>
        </thead>
        <tbody class="divide-y divide-outline-variant">
          <tr v-for="(col, idx) in editorCols" :key="idx">
            <td class="px-3 py-1.5"><input v-model="col.name" class="w-full text-[13px] border border-outline-variant px-2 py-1 outline-none focus:border-primary" /></td>
            <td class="px-3 py-1.5"><input v-model="col.key" class="w-full text-[13px] border border-outline-variant px-2 py-1 outline-none focus:border-primary font-mono" /></td>
            <td class="px-3 py-1.5 text-center"><input type="checkbox" v-model="col.show" /></td>
            <td class="px-3 py-1.5 text-center"><input type="checkbox" v-model="col.edit" /></td>
            <td class="px-3 py-1.5 text-center"><input type="checkbox" v-model="col.required" /></td>
            <td class="px-3 py-1.5 text-right"><button @click="editorCols.splice(idx, 1)" class="text-outline hover:text-error"><span class="material-symbols-outlined text-[18px]">delete</span></button></td>
          </tr>
        </tbody>
      </table>
      <button @click="editorCols.push({ name: '', key: '', show: true, edit: true, required: false })" class="text-[12px] text-primary font-medium flex items-center gap-1 hover:underline">
        <span class="material-symbols-outlined text-[16px]">add</span>添加字段
      </button>

      <template #footer>
        <button @click="editor.open = false" class="px-4 py-2 text-[13px] border border-outline-variant rounded hover:bg-surface-container-high transition">取消</button>
        <button @click="save" class="px-4 py-2 text-[13px] bg-primary text-on-primary rounded hover:opacity-90 transition">保存</button>
      </template>
    </ModalDialog>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import ModalDialog from '@/components/ModalDialog.vue'
import { SubTableApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { SUBTABLE_HEIGHT_OPTIONS, type SubTableHeightMode, type SubTableMapping } from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const subtables = ref<SubTableMapping[]>([])

interface SubCol { name: string; key: string; show: boolean; edit: boolean; required: boolean }

function parseCols(json?: string | null): SubCol[] {
  if (!json) return []
  try {
    const arr = JSON.parse(json)
    return arr.map((c: any) => ({ name: c.name ?? '', key: c.key ?? '', show: c.show ?? true, edit: c.edit ?? true, required: c.required ?? false }))
  } catch {
    return []
  }
}

// 小标签内联组件
const Tag = (_: unknown, { slots }: any) => h('span', { class: 'text-[10px] px-1.5 py-0.5 rounded-full bg-surface-container-high text-on-surface-variant' }, slots.default?.())

function heightLabel(m: SubTableHeightMode) {
  return SUBTABLE_HEIGHT_OPTIONS.find((x) => x.value === m)?.label || m
}

const blank = (): SubTableMapping => ({
  sortOrder: (subtables.value.at(-1)?.sortOrder || 0) + 1,
  subTableName: '',
  subTableCode: '',
  relationField: '',
  filterCondition: '',
  showHeader: true,
  allowAddRow: true,
  allowBatchDelete: true,
  heightMode: 'AUTO',
  fixedHeight: 240,
  columnsJson: '[]'
})

const editor = reactive<{ open: boolean; data: SubTableMapping }>({ open: false, data: blank() })
const editorCols = ref<SubCol[]>([])

function openEditor(s?: SubTableMapping) {
  editor.data = s ? { ...s } : blank()
  editorCols.value = s && s.columnsJson ? parseCols(s.columnsJson) : []
  editor.open = true
}
function setHeight(m: SubTableHeightMode) {
  editor.data.heightMode = m
}

async function load() {
  subtables.value = await SubTableApi.listByTable(tableId.value)
}

async function save() {
  const d = editor.data
  if (!d.subTableName.trim() || !d.subTableCode.trim()) {
    toast.error('子表名称与关联编码必填')
    return
  }
  d.columnsJson = JSON.stringify(editorCols.value.filter((c) => c.key.trim()))
  try {
    if (d.id) await SubTableApi.update(tableId.value, d.id, d)
    else await SubTableApi.add(tableId.value, d)
    toast.success('已保存')
    editor.open = false
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function remove(s: SubTableMapping) {
  if (!confirm(`删除子表「${s.subTableName}」？`)) return
  try {
    await SubTableApi.remove(tableId.value, s.id!)
    toast.success('已删除')
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

onMounted(load)
</script>
