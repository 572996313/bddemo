<template>
  <button
    type="button"
    @click="$emit('update:modelValue', !modelValue)"
    class="relative inline-flex h-4 w-7 items-center rounded-full transition-colors"
    :class="modelValue ? 'bg-primary' : 'bg-outline-variant'"
  >
    <span
      class="inline-block h-3 w-3 transform rounded-full bg-white transition-transform"
      :class="modelValue ? 'translate-x-3.5' : 'translate-x-0.5'"
    />
  </button>
</template>

<script setup lang="ts">
defineProps<{ modelValue: boolean }>()
defineEmits<{ (e: 'update:modelValue', v: boolean): void }>()
</script>
