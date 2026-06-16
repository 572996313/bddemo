import { createRouter, createWebHistory } from 'vue-router'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'tables', component: () => import('@/views/TableListView.vue') },
    {
      path: '/tables/:id',
      component: () => import('@/views/TableWorkspace.vue'),
      props: true,
      children: [
        { path: '', redirect: (to) => `/tables/${to.params.id}/basic` },
        { path: 'basic', name: 'table-basic', component: () => import('@/views/BasicInfoView.vue') },
        // GRID 表格类
        { path: 'columns', name: 'table-columns', component: () => import('@/views/ColumnConfigView.vue') },
        { path: 'queries', name: 'table-queries', component: () => import('@/views/QueryConfigView.vue') },
        { path: 'preview', name: 'table-preview', component: () => import('@/views/DataPreviewView.vue') },
        // FORM 表单类
        { path: 'fields', name: 'table-fields', component: () => import('@/views/FormFieldView.vue') },
        { path: 'subtables', name: 'table-subtables', component: () => import('@/views/SubTableView.vue') },
        { path: 'form-preview', name: 'table-form-preview', component: () => import('@/views/FormPreviewView.vue') }
      ]
    },
    { path: '/tables/new', name: 'table-new', component: () => import('@/views/BasicInfoView.vue') },
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})
