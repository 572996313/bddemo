<template>
  <Teleport to="body">
    <div class="fixed top-4 right-4 z-[100] flex flex-col gap-2">
      <TransitionGroup name="toast">
        <div
          v-for="t in toast.state.items"
          :key="t.id"
          @click="toast.remove(t.id)"
          class="cursor-pointer min-w-[220px] max-w-[360px] px-4 py-3 rounded shadow-lg border text-sm font-medium flex items-center gap-2"
          :class="cls(t.type)"
        >
          <span class="material-symbols-outlined text-[18px]">{{ icon(t.type) }}</span>
          <span class="flex-1">{{ t.text }}</span>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { useToast, type ToastItem } from '@/composables/useToast'
const toast = useToast()
function cls(t: ToastItem['type']) {
  return {
    success: 'bg-white border-emerald-200 text-emerald-800',
    error: 'bg-white border-red-200 text-red-700',
    info: 'bg-white border-surface-container-highest text-on-surface'
  }[t]
}
function icon(t: ToastItem['type']) {
  return { success: 'check_circle', error: 'error', info: 'info' }[t]
}
</script>

<style scoped>
.toast-enter-from {
  opacity: 0;
  transform: translateX(20px);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
