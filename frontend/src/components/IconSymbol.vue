<template>
  <span class="material-symbols-outlined" :style="{ fontSize: sizePx }" aria-hidden="true">{{ name }}</span>
</template>

<script setup lang="ts">
import { computed } from 'vue'
const props = withDefaults(
  defineProps<{ name: string; size?: number | string }>(),
  { size: 20 }
)
const sizePx = computed(() => (typeof props.size === 'number' ? `${props.size}px` : props.size))
</script>
