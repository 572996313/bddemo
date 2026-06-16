<template>
  <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
    <div class="lg:col-span-2 space-y-5">
      <!-- 表单主体 -->
      <SectionCard v-for="g in groups" :key="g.name" :title="g.name" icon="folder_open">
        <div class="grid grid-cols-2 gap-x-5 gap-y-4">
          <template v-for="f in g.fields" :key="f.code">
            <!-- 子表字段：跨整行，渲染嵌入明细表 -->
            <div v-if="f.controlType === 'SUBTABLE'" class="col-span-2">
              <SubTableBlock :sub="subtableFor(f)" v-model:rows="subRows[f.code]" />
            </div>
            <!-- 普通字段 -->
            <div v-else :class="f.layoutWeight === 2 ? 'col-span-2' : 'col-span-1'">
              <label class="flex items-center gap-1 text-[12px] font-medium text-on-surface mb-1">
                {{ f.label }}
                <span v-if="f.required" class="text-error">*</span>
                <span v-if="f.readOnly" class="text-[10px] text-outline">(只读)</span>
              </label>
              <FormControl :field="f" v-model="model[f.code]" />
            </div>
          </template>
        </div>
      </SectionCard>
    </div>

    <!-- 侧边：按钮 + 校验 -->
    <div class="space-y-5">
      <SectionCard title="表单操作" icon="touch_app">
        <button
          v-for="a in actions"
          :key="a.code"
          @click="onAction(a)"
          class="w-full mb-2 py-2 text-[13px] font-medium flex items-center justify-center gap-1.5"
          :class="actionClass(a.style)"
        >
          <span class="material-symbols-outlined text-[18px]">{{ a.icon || 'extension' }}</span>
          {{ a.name }}
        </button>
        <button @click="validateAll" class="w-full mb-2 py-2 text-[13px] border border-outline-variant text-on-surface-variant rounded hover:bg-surface-container-high transition flex items-center justify-center gap-1.5">
          <span class="material-symbols-outlined text-[18px]">rule</span>校验必填项
        </button>
      </SectionCard>

      <SectionCard title="填写预览值" icon="data_object">
        <pre class="text-[11px] font-mono bg-surface-container p-3 rounded max-h-72 overflow-auto text-on-surface-variant">{{ JSON.stringify(collectModel(), null, 2) }}</pre>
      </SectionCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import SectionCard from '@/components/SectionCard.vue'
import FormControl from '@/components/FormControl.vue'
import SubTableBlock from '@/components/SubTableBlock.vue'
import { FormPreviewApi } from '@/api'
import { useToast } from '@/composables/useToast'

const route = useRoute()
const toast = useToast()
const tableId = computed(() => String(route.params.id))

const groups = ref<any[]>([])
const subtables = ref<any[]>([])
const actions = ref<any[]>([])
const model = reactive<Record<string, any>>({})
const subRows = reactive<Record<string, any[]>>({})

function subtableFor(f: any) {
  return subtables.value[0] || null
}

function collectModel() {
  const out: Record<string, any> = { ...model }
  for (const k of Object.keys(subRows)) out[k] = subRows[k]
  return out
}

function actionClass(style: string) {
  return {
    PRIMARY: 'bg-primary text-on-primary rounded',
    GHOST: 'border border-outline-variant text-on-surface-variant rounded',
    DANGER: 'border-2 border-error text-error rounded',
    LINK: 'text-secondary hover:underline'
  }[style] || 'border border-outline-variant text-on-surface-variant rounded'
}

function onAction(a: any) {
  toast.info(`触发「${a.name}」 → ${a.handler || a.code}`)
}

function validateAll() {
  const missing: string[] = []
  for (const g of groups.value) {
    for (const f of g.fields) {
      if (f.controlType === 'SUBTABLE') continue
      if (f.required) {
        const v = model[f.code]
        if (v === undefined || v === null || v === '' || (Array.isArray(v) && !v.length)) {
          missing.push(f.label)
        }
      }
    }
  }
  if (missing.length) toast.error(`缺少必填项：${missing.join('、')}`)
  else toast.success('必填项校验通过')
}

async function load() {
  const data = await FormPreviewApi.preview(tableId.value)
  groups.value = data.groups || []
  subtables.value = data.subTables || []
  actions.value = data.actions || []
  for (const g of groups.value) {
    for (const f of g.fields) {
      model[f.code] = f.value
      if (f.controlType === 'SUBTABLE') {
        subRows[f.code] = subtables.value[0] ? structuredCloneSafe(subtables.value[0].rows) : []
      }
    }
  }
}

function structuredCloneSafe(v: any) {
  try { return JSON.parse(JSON.stringify(v)) } catch { return v }
}

onMounted(load)
</script>
