<template>
  <div class="min-h-screen flex bg-background text-on-surface">
    <!-- SideNavBar -->
    <aside
      class="fixed left-0 top-0 h-screen w-panel-width bg-white border-r border-outline-variant flex flex-col py-4 px-2 z-50"
    >
      <div class="mb-8 px-2">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-primary-container rounded flex items-center justify-center">
            <span class="material-symbols-outlined text-on-primary-container">precision_manufacturing</span>
          </div>
          <div>
            <h1 class="text-xl font-extrabold text-on-surface">表格运维</h1>
            <p class="text-[11px] uppercase tracking-wider text-on-surface-variant">Table Ops Console</p>
          </div>
        </div>
      </div>

      <div class="px-2 mb-3">
        <RouterLink
          to="/"
          class="w-full py-2 px-4 bg-primary text-on-primary font-bold rounded flex items-center justify-center gap-2 hover:opacity-90 transition"
        >
          <span class="material-symbols-outlined">add</span>
          <span>新建表格配置</span>
        </RouterLink>
      </div>

      <nav class="flex-1 space-y-1">
        <RouterLink
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          class="flex items-center gap-3 px-4 py-2 text-on-surface-variant hover:bg-surface-container-low rounded-lg transition-colors"
          active-class="!bg-secondary-container !text-on-secondary-container font-bold translate-x-1"
        >
          <span class="material-symbols-outlined">{{ item.icon }}</span>
          <span class="text-[13px] uppercase tracking-wide font-medium">{{ item.label }}</span>
        </RouterLink>
      </nav>

      <div class="border-t border-outline-variant pt-3 px-4">
        <div class="flex items-center gap-2 text-[11px] text-outline">
          <span class="material-symbols-outlined text-[16px]">verified</span>
          <span>元数据驱动 · v1.0</span>
        </div>
      </div>
    </aside>

    <!-- Main -->
    <main class="ml-[320px] flex-1 flex flex-col min-h-screen">
      <header
        class="h-toolbar-height bg-surface border-b border-outline-variant flex justify-between items-center px-6 sticky top-0 z-40"
      >
        <div class="flex items-center gap-2">
          <slot name="topbar-left">
            <h2 class="text-base font-bold text-primary">{{ title }}</h2>
          </slot>
        </div>
        <div class="flex items-center gap-3">
          <slot name="topbar-right" />
        </div>
      </header>

      <div class="p-6 flex-1">
        <slot />
      </div>
    </main>

    <Toaster />
  </div>
</template>

<script setup lang="ts">
import Toaster from './Toaster.vue'

defineProps<{ title?: string }>()

const navItems = [
  { to: '/', label: '表格配置', icon: 'view_list' },
  { to: '/tables/1/preview', label: '资产主表查询', icon: 'analytics' },
  { to: '/tables/2/preview', label: '汽运订单查询', icon: 'local_shipping' }
]
</script>
