<template>
  <div class="w-full">
    <!-- 只读展示 -->
    <div v-if="field.readOnly && field.controlType !== 'SWITCH'" class="text-sm text-on-surface-variant bg-surface-container px-3 py-2 rounded">
      {{ displayValue }}
    </div>

    <input
      v-else-if="field.controlType === 'TEXT'"
      v-model="model"
      type="text"
      :placeholder="field.placeholder"
      class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none rounded"
    />
    <input
      v-else-if="field.controlType === 'NUMBER'"
      v-model="model"
      type="number"
      :placeholder="field.placeholder"
      class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none rounded"
    />
    <textarea
      v-else-if="field.controlType === 'TEXTAREA'"
      v-model="model"
      rows="3"
      :placeholder="field.placeholder"
      class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none rounded"
    />
    <select
      v-else-if="field.controlType === 'SELECT' || field.controlType === 'REF'"
      v-model="model"
      class="w-full text-sm border border-outline-variant px-3 py-2 outline-none focus:border-primary bg-white rounded"
    >
      <option value="">{{ field.controlType === 'REF' ? '请选择...' : '请选择' }}</option>
      <option v-for="o in optionList" :key="o" :value="o">{{ o }}</option>
    </select>
    <div v-else-if="field.controlType === 'RADIO'" class="flex flex-wrap gap-3 pt-1.5">
      <label v-for="o in optionList" :key="o" class="flex items-center gap-1 text-[13px] cursor-pointer">
        <input type="radio" :value="o" v-model="model" /> {{ o }}
      </label>
    </div>
    <div v-else-if="field.controlType === 'CHECKBOX'" class="flex flex-wrap gap-3 pt-1.5">
      <label v-for="o in optionList" :key="o" class="flex items-center gap-1 text-[13px] cursor-pointer">
        <input type="checkbox" :value="o" v-model="model" /> {{ o }}
      </label>
    </div>
    <input
      v-else-if="field.controlType === 'DATE'"
      v-model="model"
      type="date"
      class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none rounded"
    />
    <input
      v-else-if="field.controlType === 'DATETIME'"
      v-model="model"
      type="datetime-local"
      class="w-full text-sm border border-outline-variant px-3 py-2 focus:border-primary outline-none rounded"
    />
    <div v-else-if="field.controlType === 'SWITCH'" class="flex items-center gap-2 pt-1">
      <button type="button" @click="model = !model" class="relative inline-flex h-5 w-9 items-center rounded-full transition-colors" :class="model ? 'bg-primary' : 'bg-outline-variant'">
        <span class="inline-block h-4 w-4 transform rounded-full bg-white transition-transform" :class="model ? 'translate-x-4' : 'translate-x-0.5'" />
      </button>
      <span class="text-[13px] text-on-surface-variant">{{ model ? '是' : '否' }}</span>
    </div>
    <div v-else-if="field.controlType === 'RATING'" class="flex items-center gap-1 pt-1">
      <button v-for="n in 5" :key="n" type="button" @click="model = n" @mouseenter="hover = n" @mouseleave="hover = 0">
        <span class="material-symbols-outlined" :class="n <= (hover || model) ? 'text-amber-500' : 'text-outline'">{{ n <= (hover || model) ? 'star' : 'grade' }}</span>
      </button>
      <span class="text-[12px] text-on-surface-variant ml-1">{{ model }}/5</span>
    </div>
    <div v-else class="text-[12px] text-outline">未支持的控件：{{ field.controlType }}</div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'

const props = defineProps<{ field: any }>()
const model = defineModel<any>({ default: null })
const hover = ref(0)

const optionList = computed(() => {
  if (props.field.controlType === 'REF') return props.field.refOptions || []
  return props.field.options || []
})

const displayValue = computed(() => {
  const v = model.value
  if (Array.isArray(v)) return v.join(', ')
  if (typeof v === 'boolean') return v ? '是' : '否'
  return v ?? '—'
})
</script>
