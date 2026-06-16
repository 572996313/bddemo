<template>
  <div class="bg-white border border-outline-variant">
    <header
      v-if="title || $slots.actions"
      class="px-4 py-2.5 border-b border-outline-variant flex items-center justify-between"
      :class="muted ? 'bg-[#F8FAFC]' : ''"
    >
      <div class="flex items-center gap-2">
        <span v-if="icon" class="material-symbols-outlined text-on-surface-variant text-[20px]">{{ icon }}</span>
        <h3 class="text-[13px] font-bold uppercase tracking-wider text-on-surface-variant">{{ title }}</h3>
        <span v-if="hint" class="text-[12px] text-outline normal-case tracking-normal">· {{ hint }}</span>
      </div>
      <div v-if="$slots.actions" class="flex items-center gap-2"><slot name="actions" /></div>
    </header>
    <div class="p-5">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ title?: string; icon?: string; hint?: string; muted?: boolean }>()
</script>
