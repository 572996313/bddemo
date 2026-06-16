<template>
  <Teleport to="body">
    <div class="fixed inset-0 z-[90] flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-[1px]" @click="$emit('close')"></div>
      <div class="relative bg-white border border-outline-variant w-full max-w-2xl shadow-2xl">
        <header class="px-5 py-3 border-b border-outline-variant flex items-center justify-between bg-[#F8FAFC]">
          <h3 class="text-[13px] font-bold uppercase tracking-wider text-on-surface">{{ title }}</h3>
          <button @click="$emit('close')" class="text-outline hover:text-on-surface">
            <span class="material-symbols-outlined">close</span>
          </button>
        </header>
        <div class="p-5 max-h-[70vh] overflow-y-auto">
          <slot />
        </div>
        <footer v-if="$slots.footer" class="px-5 py-3 border-t border-outline-variant flex justify-end gap-2 bg-[#F8FAFC]">
          <slot name="footer" />
        </footer>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
defineProps<{ title: string }>()
defineEmits<{ (e: 'close'): void }>()
</script>
