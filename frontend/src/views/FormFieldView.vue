<template>
  <div class="space-y-5">
    <SectionCard title="表单字段配置" icon="list_alt" hint="按分组管理录入字段，支持移动排序" muted>
      <template #actions>
        <button
          @click="openEditor()"
          class="text-[12px] uppercase tracking-wide font-medium px-3 py-1 bg-primary text-on-primary rounded hover:opacity-90 transition flex items-center gap-1"
        >
          <span class="material-symbols-outlined text-[16px]">add</span>添加字段
        </button>
      </template>

      <div v-if="!fields.length" class="px-4 py-12 text-center text-on-surface-variant text-sm">
        暂无字段，点击「添加字段」开始配置表单
      </div>

      <!-- 按分组渲染 -->
      <div v-for="g in grouped" :key="g.name" class="mb-5">
        <div class="flex items-center gap-2 mb-2 px-1">
          <span class="material-symbols-outlined text-on-surface-variant text-[18px]">folder</span>
          <h4 class="text-[13px] font-bold text-on-surface uppercase tracking-wide">{{ g.name }}</h4>
          <span class="text-[11px] text-outline">{{ g.items.length }} 项</span>
          <div class="flex-1 border-b border-dashed border-outline-variant"></div>
        </div>
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-[#F1F5F9] border-y border-outline-variant">
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant w-10">序</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">字段标签</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">编码</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant">控件</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant w-16">必填</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant w-20">布局</th>
              <th class="px-3 py-2 text-[11px] font-bold uppercase text-on-surface-variant text-right w-28">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant">
            <tr v-for="f in g.items" :key="f.id" class="hover:bg-surface-container-lowest transition">
              <td class="px-3 py-2.5 text-[12px] text-outline font-mono">{{ f.sortOrder }}</td>
              <td class="px-3 py-2.5 text-sm font-semibold text-on-background">
                {{ f.fieldLabel }}
                <span v-if="f.readOnly" class="ml-1 text-[10px] text-outline">(只读)</span>
              </td>
              <td class="px-3 py-2.5"><code class="font-mono text-[12px] bg-surface-container px-1.5 py-0.5 rounded text-secondary">{{ f.fieldCode }}</code></td>
              <td class="px-3 py-2.5">
                <span class="inline-flex items-center gap-1 text-[12px] px-2 py-0.5 rounded-full"
                  :class="ctrlChipClass(f.controlType)">
                  <span class="material-symbols-outlined text-[14px]">{{ ctrlIcon(f.controlType) }}</span>
                  {{ ctrlLabel(f.controlType) }}
                </span>
              </td>
              <td class="px-3 py-2.5">
                <span v-if="f.required" class="material-symbols-outlined text-error text-[18px]">priority_high</span>
                <span v-else class="text-outline">—</span>
              </td>
              <td class="px-3 py-2.5 text-[12px] text-on-surface-variant">{{ f.layoutWeight === 2 ? '整行' : '半行' }}</td>
              <td class="px-3 py-2.5 text-right whitespace-nowrap">
                <button @click="move(f, -1)" class="p-1 text-outline hover:text-primary transition" title="上移"><span class="material-symbols-outlined text-[18px]">arrow_upward</span></button>
                <button @click="move(f, 1)" class="p-1 text-outline hover:text-primary transition" title="下移"><span class="material-symbols-outlined text-[18px]">arrow_downward</span></button>
                <button @click="openEditor(f)" class="p-1 text-outline hover:text-primary transition ml-1"><span class="material-symbols-outlined text-[18px]">edit</span></button>
                <button @click="remove(f)" class="p-1 text-outline hover:text-error transition ml-1"><span class="material-symbols-outlined text-[18px]">delete</span></button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>

    <!-- 编辑器 -->
    <ModalDialog v-if="editor.open" :title="editor.data.id ? '编辑字段' : '新增字段'" @close="editor.open = false">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">所属分组</label>
          <input v-model="editor.data.groupName" list="group-list" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" placeholder="如：基础信息" />
          <datalist id="group-list">
            <option v-for="g in groupNames" :key="g" :value="g" />
          </datalist>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">控件类型</label>
          <select v-model="editor.data.controlType" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
            <option v-for="c in FORM_CONTROL_OPTIONS" :key="c.value" :value="c.value">{{ c.label }}</option>
          </select>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">字段标签 *</label>
          <input v-model="editor.data.fieldLabel" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">字段编码 *</label>
          <input v-model="editor.data.fieldCode" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="snake_case" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">布局权重</label>
          <div class="flex gap-2">
            <button type="button" @click="editor.data.layoutWeight = 1" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.layoutWeight === 1 ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">半行</button>
            <button type="button" @click="editor.data.layoutWeight = 2" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.layoutWeight === 2 ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">整行</button>
          </div>
        </div>
        <div class="grid grid-cols-3 gap-2 content-center">
          <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.required" /> 必填</label>
          <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.visible" /> 可见</label>
          <label class="flex items-center gap-1.5 text-sm"><input type="checkbox" v-model="editor.data.readOnly" /> 只读</label>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">默认值</label>
          <input v-model="editor.data.defaultValue" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">占位符</label>
          <input v-model="editor.data.placeholder" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div v-if="needOptions">
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">选项（逗号分隔）</label>
          <input v-model="editor.data.options" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" placeholder="低,中,高" />
        </div>
        <div v-if="editor.data.controlType === 'REF'">
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">关联实体编码</label>
          <input v-model="editor.data.refEntity" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="T_CUSTOMER" />
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
import { FieldApi } from '@/api'
import { useToast } from '@/composables/useToast'
import { FORM_CONTROL_OPTIONS, type FormControlType, type FormField } from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const fields = ref<FormField[]>([])

const grouped = computed(() => {
  const map = new Map<string, FormField[]>()
  for (const f of fields.value) {
    const g = f.groupName || '默认分组'
    if (!map.has(g)) map.set(g, [])
    map.get(g)!.push(f)
  }
  return Array.from(map, ([name, items]) => ({ name, items }))
})

const groupNames = computed(() => grouped.value.map((g) => g.name))

function ctrlLabel(c: FormControlType) {
  return FORM_CONTROL_OPTIONS.find((x) => x.value === c)?.label || c
}
function ctrlIcon(c: FormControlType) {
  return FORM_CONTROL_OPTIONS.find((x) => x.value === c)?.icon || 'extension'
}
function ctrlChipClass(c: FormControlType) {
  const map: Record<string, string> = {
    TEXT: 'bg-blue-50 text-blue-700', NUMBER: 'bg-emerald-50 text-emerald-700',
    TEXTAREA: 'bg-indigo-50 text-indigo-700', SELECT: 'bg-cyan-50 text-cyan-700',
    RADIO: 'bg-amber-50 text-amber-700', CHECKBOX: 'bg-orange-50 text-orange-700',
    DATE: 'bg-teal-50 text-teal-700', DATETIME: 'bg-teal-50 text-teal-700',
    SWITCH: 'bg-purple-50 text-purple-700', RATING: 'bg-yellow-50 text-yellow-700',
    REF: 'bg-rose-50 text-rose-700', SUBTABLE: 'bg-slate-100 text-slate-700'
  }
  return map[c] || 'bg-surface-container text-on-surface-variant'
}

const blank = (): FormField => ({
  sortOrder: (fields.value.at(-1)?.sortOrder || 0) + 1,
  groupName: groupNames.value[0] || '基础信息',
  fieldLabel: '',
  fieldCode: '',
  controlType: 'TEXT',
  layoutWeight: 1,
  required: false,
  visible: true,
  readOnly: false,
  defaultValue: '',
  placeholder: '',
  options: '',
  refEntity: ''
})

const editor = reactive<{ open: boolean; data: FormField }>({ open: false, data: blank() })
const needOptions = computed(() => ['SELECT', 'RADIO', 'CHECKBOX'].includes(editor.data.controlType))

function openEditor(f?: FormField) {
  editor.data = f ? { ...f } : blank()
  editor.open = true
}

async function load() {
  fields.value = await FieldApi.listByTable(tableId.value)
}

async function save() {
  const d = editor.data
  if (!d.fieldLabel.trim() || !d.fieldCode.trim()) {
    toast.error('字段标签与编码必填')
    return
  }
  try {
    if (d.id) await FieldApi.update(tableId.value, d.id, d)
    else await FieldApi.add(tableId.value, d)
    toast.success('已保存')
    editor.open = false
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function move(f: FormField, dir: number) {
  try {
    await FieldApi.move(tableId.value, f.id!, dir)
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function remove(f: FormField) {
  if (!confirm(`删除字段「${f.fieldLabel}」？`)) return
  try {
    await FieldApi.remove(tableId.value, f.id!)
    toast.success('已删除')
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

onMounted(load)
</script>
