<template>
  <div class="space-y-5">
    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">按钮总数</p>
        <span class="text-4xl font-extrabold text-primary leading-none block mt-2">{{ buttons.length }}</span>
      </div>
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">表头操作</p>
        <div class="flex items-end justify-between mt-2">
          <span class="text-4xl font-extrabold text-primary leading-none">{{ headerCount }}</span>
          <span class="text-[12px] text-on-surface-variant">全局范围</span>
        </div>
      </div>
      <div class="bg-white border border-outline-variant p-4">
        <p class="text-[11px] uppercase tracking-wider text-on-surface-variant font-medium">行内操作</p>
        <div class="flex items-end justify-between mt-2">
          <span class="text-4xl font-extrabold text-primary leading-none">{{ rowCount }}</span>
          <span class="text-[12px] text-on-surface-variant">记录范围</span>
        </div>
      </div>
      <button
        @click="openEditor()"
        class="bg-white border border-outline-variant p-4 flex flex-col justify-center items-center cursor-pointer hover:bg-surface-container-low transition group"
      >
        <div class="w-10 h-10 bg-primary-container text-on-primary-container rounded-full flex items-center justify-center group-hover:scale-110 transition">
          <span class="material-symbols-outlined">add</span>
        </div>
        <span class="text-[12px] font-bold text-primary mt-1">添加按钮</span>
      </button>
    </div>

    <!-- 按钮表 -->
    <SectionCard title="已定义的表格操作" icon="smart_button" hint="表格按钮功能维护" muted>
      <template #actions>
        <div class="relative">
          <span class="material-symbols-outlined absolute left-2 top-1/2 -translate-y-1/2 text-on-surface-variant text-[18px]">search</span>
          <input
            v-model="keyword"
            class="pl-8 pr-3 py-1 text-[13px] bg-white border border-outline-variant focus:border-primary outline-none w-56"
            placeholder="搜索按钮..."
          />
        </div>
      </template>

      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-[#F1F5F9] border-y border-outline-variant">
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">启用</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">按钮名称</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">图标</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">操作类型</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">位置</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant">样式</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right">预览</th>
              <th class="px-4 py-2 text-[12px] font-bold uppercase text-on-surface-variant text-right">管理</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant">
            <tr v-for="b in filtered" :key="b.id" class="hover:bg-surface-container-lowest transition">
              <td class="px-4 py-3">
                <ToggleSwitch :model-value="b.enabled" @update:model-value="toggleEnable(b, $event)" />
              </td>
              <td class="px-4 py-3">
                <div class="flex flex-col">
                  <span class="text-sm font-semibold text-on-background">{{ b.name }}</span>
                  <span class="text-[12px] text-outline font-mono">{{ b.code }}</span>
                </div>
              </td>
              <td class="px-4 py-3">
                <span class="material-symbols-outlined" :class="iconColor(b.actionType)">{{ b.icon || 'extension' }}</span>
              </td>
              <td class="px-4 py-3">
                <span class="px-2 py-0.5 rounded-full text-[11px] font-medium" :class="actionChip(b.actionType)">
                  {{ actionLabel(b.actionType) }}
                </span>
              </td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ b.position === 'HEADER' ? '表头' : '行内' }}</td>
              <td class="px-4 py-3 text-[13px] text-on-surface-variant">{{ styleLabel(b.buttonStyle) }}</td>
              <td class="px-4 py-3 text-right">
                <span class="inline-flex">
                  <component :is="renderPreview(b)" />
                </span>
              </td>
              <td class="px-4 py-3 text-right">
                <button @click="openEditor(b)" class="p-1 text-outline hover:text-primary transition"><span class="material-symbols-outlined">edit</span></button>
                <button @click="remove(b)" class="p-1 text-outline hover:text-error transition ml-1"><span class="material-symbols-outlined">delete</span></button>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="8" class="px-4 py-10 text-center text-on-surface-variant text-sm">暂无按钮，点击「添加按钮」配置表格功能</td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>

    <!-- 编辑器 -->
    <ModalDialog v-if="editor.open" :title="editor.data.id ? '编辑按钮' : '新增按钮'" @close="editor.open = false">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">按钮名称 *</label>
          <input v-model="editor.data.name" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">按钮编码 *</label>
          <input v-model="editor.data.code" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="btn_xxx" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">图标标识</label>
          <div class="flex gap-2">
            <input v-model="editor.data.icon" class="flex-1 text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="add_circle" />
            <span class="w-10 border border-outline-variant flex items-center justify-center"><span class="material-symbols-outlined text-on-surface-variant">{{ editor.data.icon || 'extension' }}</span></span>
          </div>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">事件处理函数</label>
          <input v-model="editor.data.eventHandler" class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none font-mono" placeholder="handleClick" />
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">操作类型</label>
          <select v-model="editor.data.actionType" class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white">
            <option v-for="a in ACTION_TYPE_OPTIONS" :key="a.value" :value="a.value">{{ a.label }}</option>
          </select>
        </div>
        <div>
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">位置</label>
          <div class="flex gap-2">
            <button type="button" @click="editor.data.position = 'HEADER'" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.position === 'HEADER' ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">表头</button>
            <button type="button" @click="editor.data.position = 'ROW'" class="flex-1 py-2 text-[13px] border-2" :class="editor.data.position === 'ROW' ? 'border-primary bg-primary text-on-primary' : 'border-outline-variant text-on-surface-variant'">行内</button>
          </div>
        </div>
        <div class="md:col-span-2">
          <label class="block text-[11px] uppercase tracking-wide text-on-surface-variant mb-1">按钮样式</label>
          <div class="flex gap-2">
            <button
              v-for="s in BUTTON_STYLE_OPTIONS"
              :key="s.value"
              type="button"
              @click="editor.data.buttonStyle = s.value"
              class="flex-1 py-2 text-[13px] border-2"
              :class="styleBtnClass(s.value, editor.data.buttonStyle === s.value)"
            >{{ s.label }}</button>
          </div>
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
import { computed, h, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import ModalDialog from '@/components/ModalDialog.vue'
import ToggleSwitch from '@/components/ToggleSwitch.vue'
import { ButtonApi } from '@/api'
import { useToast } from '@/composables/useToast'
import {
  ACTION_TYPE_OPTIONS,
  BUTTON_STYLE_OPTIONS,
  type ActionType,
  type ButtonStyle,
  type TableButton
} from '@/types'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const buttons = ref<TableButton[]>([])
const keyword = ref('')

const headerCount = computed(() => buttons.value.filter((b) => b.position === 'HEADER').length)
const rowCount = computed(() => buttons.value.filter((b) => b.position === 'ROW').length)
const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return buttons.value
  return buttons.value.filter((b) => b.name.toLowerCase().includes(k) || b.code.toLowerCase().includes(k))
})

function actionLabel(a: ActionType) {
  return ACTION_TYPE_OPTIONS.find((x) => x.value === a)?.label || a
}
function styleLabel(s: ButtonStyle) {
  return BUTTON_STYLE_OPTIONS.find((x) => x.value === s)?.label || s
}
function actionChip(a: ActionType) {
  const map: Record<string, string> = {
    ADD: 'bg-blue-100 text-blue-900',
    EDIT: 'bg-gray-100 text-gray-900',
    DELETE: 'bg-red-100 text-red-900',
    EXPORT: 'bg-emerald-100 text-emerald-900',
    IMPORT: 'bg-amber-100 text-amber-900',
    CUSTOM: 'bg-purple-100 text-purple-900'
  }
  return map[a] || 'bg-surface-container text-on-surface-variant'
}
function iconColor(a: ActionType) {
  const map: Record<string, string> = {
    ADD: 'text-secondary',
    EDIT: 'text-primary',
    DELETE: 'text-error',
    EXPORT: 'text-emerald-700',
    IMPORT: 'text-amber-700',
    CUSTOM: 'text-purple-700'
  }
  return map[a] || 'text-on-surface-variant'
}

// 实时预览：按样式渲染一个真实按钮（返回渲染函数）
function renderPreview(b: TableButton) {
  return () => {
    const icon = h('span', { class: 'material-symbols-outlined text-[16px]' }, b.icon || 'extension')
    const label = b.name
    const base = 'px-3 py-1 text-[12px] font-medium flex items-center gap-1 ml-auto'
    const cls: Record<ButtonStyle, string> = {
      PRIMARY: 'bg-primary text-on-primary',
      GHOST: 'border-2 border-outline-variant text-on-surface-variant',
      DANGER: 'border-2 border-error text-error',
      LINK: 'text-primary hover:underline'
    }
    return h('button', { class: `${base} ${cls[b.buttonStyle]}` }, [icon, label])
  }
}

function styleBtnClass(style: ButtonStyle, active: boolean) {
  if (active) {
    const map: Record<ButtonStyle, string> = {
      PRIMARY: 'border-primary bg-primary text-on-primary',
      GHOST: 'border-on-surface-variant text-on-surface',
      DANGER: 'border-error text-error',
      LINK: 'border-secondary text-secondary'
    }
    return map[style]
  }
  return 'border-outline-variant text-on-surface-variant'
}

const blank = (): TableButton => ({
  sortOrder: (buttons.value.at(-1)?.sortOrder || 0) + 1,
  name: '',
  code: '',
  icon: 'extension',
  actionType: 'CUSTOM',
  position: 'HEADER',
  buttonStyle: 'PRIMARY',
  enabled: true,
  eventHandler: ''
})

const editor = reactive<{ open: boolean; data: TableButton }>({ open: false, data: blank() })

function openEditor(b?: TableButton) {
  editor.data = b ? { ...b } : blank()
  editor.open = true
}

async function load() {
  buttons.value = await ButtonApi.listByTable(tableId.value)
}

async function save() {
  const d = editor.data
  if (!d.name.trim() || !d.code.trim()) {
    toast.error('按钮名称与编码必填')
    return
  }
  try {
    if (d.id) await ButtonApi.update(tableId.value, d.id, d)
    else await ButtonApi.add(tableId.value, d)
    toast.success('已保存')
    editor.open = false
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function toggleEnable(b: TableButton, enabled: boolean) {
  try {
    await ButtonApi.toggle(tableId.value, b.id!, enabled)
    b.enabled = enabled
  } catch (e: any) {
    toast.error(e.message)
  }
}

async function remove(b: TableButton) {
  if (!confirm(`删除按钮「${b.name}」？`)) return
  try {
    await ButtonApi.remove(tableId.value, b.id!)
    toast.success('已删除')
    await load()
    router.replace({ query: { reload: Date.now() } })
  } catch (e: any) {
    toast.error(e.message)
  }
}

onMounted(load)
</script>
