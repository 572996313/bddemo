<template>
  <div v-if="sub" class="border border-outline-variant rounded">
    <div class="px-3 py-2 bg-[#F8FAFC] border-b border-outline-variant flex items-center justify-between">
      <div class="flex items-center gap-2">
        <span class="material-symbols-outlined text-secondary text-[18px]">table_rows</span>
        <span class="text-[13px] font-bold text-on-surface">{{ sub.name }}</span>
        <code class="font-mono text-[11px] text-outline">{{ sub.code }}</code>
        <span v-if="sub.relationField" class="text-[11px] text-outline">· 关联 {{ sub.relationField }}</span>
      </div>
      <div class="flex items-center gap-2">
        <span class="text-[11px] text-on-surface-variant">共 {{ rows.length }} 行</span>
        <button v-if="sub.allowAddRow" @click="addRow" class="text-[12px] text-primary font-medium flex items-center gap-0.5 hover:underline">
          <span class="material-symbols-outlined text-[16px]">add</span>新增行
        </button>
        <button v-if="sub.allowBatchDelete" @click="clearAll" class="text-[12px] text-error font-medium flex items-center gap-0.5 hover:underline">
          <span class="material-symbols-outlined text-[16px]">delete_sweep</span>清空
        </button>
      </div>
    </div>
    <div class="overflow-x-auto" :style="sub.heightMode === 'FIXED' && sub.fixedHeight ? { maxHeight: sub.fixedHeight + 'px' } : null">
      <table class="w-full text-left border-collapse">
        <thead v-if="sub.showHeader" class="sticky top-0">
          <tr class="bg-[#F1F5F9] border-b border-outline-variant">
            <th class="px-2 py-1.5 text-[11px] font-bold uppercase text-on-surface-variant w-8">#</th>
            <th v-for="c in editableCols" :key="c.key" class="px-2 py-1.5 text-[11px] font-bold uppercase text-on-surface-variant">
              {{ c.name }}<span v-if="c.required" class="text-error">*</span>
            </th>
            <th class="px-2 py-1.5 w-10"></th>
          </tr>
        </thead>
        <tbody class="divide-y divide-outline-variant">
          <tr v-for="(row, idx) in rows" :key="idx" class="hover:bg-surface-container-lowest">
            <td class="px-2 py-1 text-[11px] text-outline font-mono">{{ idx + 1 }}</td>
            <td v-for="c in editableCols" :key="c.key" class="px-1.5 py-1">
              <input
                v-model="row[c.key]"
                :readonly="!c.edit"
                class="w-full text-[13px] px-2 py-1 border border-transparent focus:border-primary outline-none rounded"
                :class="!c.edit ? 'bg-surface-container text-on-surface-variant' : 'bg-white'"
              />
            </td>
            <td class="px-2 py-1 text-right">
              <button v-if="sub.allowBatchDelete" @click="rows.splice(idx, 1)" class="text-outline hover:text-error">
                <span class="material-symbols-outlined text-[16px]">close</span>
              </button>
            </td>
          </tr>
          <tr v-if="!rows.length">
            <td :colspan="editableCols.length + 2" class="px-4 py-6 text-center text-[12px] text-on-surface-variant">暂无明细行</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ sub: any }>()
const rows = defineModel<any[]>('rows', { default: () => [] })

const editableCols = computed(() => (props.sub?.columns || []).filter((c: any) => c.show))

function addRow() {
  const row: Record<string, any> = {}
  for (const c of editableCols.value) row[c.key] = ''
  rows.value.push(row)
}
function clearAll() {
  rows.value.splice(0, rows.value.length)
}
</script>
