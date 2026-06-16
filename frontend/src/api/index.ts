import { http } from './client'
import type {
  ColumnConfig,
  FormField,
  QueryCondition,
  SubTableMapping,
  TableButton,
  TableConfigDetail,
  TableConfigSummary,
  TableStatus
} from '@/types'

// ===== 表格聚合根 =====
export const TableApi = {
  list: () => http.get<unknown, TableConfigSummary[]>('/tables'),
  detail: (id: number | string) => http.get<unknown, TableConfigDetail>(`/tables/${id}`),
  create: (payload: Partial<TableConfigDetail>) => http.post<unknown, TableConfigDetail>('/tables', payload),
  updateBasic: (id: number | string, payload: Partial<TableConfigDetail>) =>
    http.put<unknown, TableConfigDetail>(`/tables/${id}`, payload),
  saveFull: (id: number | string, payload: TableConfigDetail) =>
    http.put<unknown, TableConfigDetail>(`/tables/${id}/full`, payload),
  remove: (id: number | string) => http.delete<unknown, void>(`/tables/${id}`),
  updateStatus: (id: number | string, status: TableStatus) =>
    http.put<unknown, TableConfigDetail>(`/tables/${id}/status`, null, { params: { status } })
}

// ===== 字段列 =====
export const ColumnApi = {
  listByTable: async (tableId: number | string) => (await TableApi.detail(tableId)).columns,
  add: (tableId: number | string, payload: ColumnConfig) =>
    http.post<unknown, ColumnConfig>(`/tables/${tableId}/columns`, payload),
  update: (tableId: number | string, columnId: number, payload: ColumnConfig) =>
    http.put<unknown, ColumnConfig>(`/tables/${tableId}/columns/${columnId}`, payload),
  remove: (tableId: number | string, columnId: number) =>
    http.delete<unknown, void>(`/tables/${tableId}/columns/${columnId}`),
  reorder: (tableId: number | string, items: { id: number; sortOrder: number }[]) =>
    http.put<unknown, void>(`/tables/${tableId}/columns/reorder`, { items })
}

// ===== 查询条件 =====
export const QueryApi = {
  listByTable: async (tableId: number | string) => (await TableApi.detail(tableId)).queryConditions,
  add: (tableId: number | string, payload: QueryCondition) =>
    http.post<unknown, QueryCondition>(`/tables/${tableId}/queries`, payload),
  update: (tableId: number | string, queryId: number, payload: QueryCondition) =>
    http.put<unknown, QueryCondition>(`/tables/${tableId}/queries/${queryId}`, payload),
  remove: (tableId: number | string, queryId: number) =>
    http.delete<unknown, void>(`/tables/${tableId}/queries/${queryId}`)
}

// ===== 按钮 =====
export const ButtonApi = {
  listByTable: async (tableId: number | string) => (await TableApi.detail(tableId)).buttons,
  add: (tableId: number | string, payload: TableButton) =>
    http.post<unknown, TableButton>(`/tables/${tableId}/buttons`, payload),
  update: (tableId: number | string, buttonId: number, payload: TableButton) =>
    http.put<unknown, TableButton>(`/tables/${tableId}/buttons/${buttonId}`, payload),
  remove: (tableId: number | string, buttonId: number) =>
    http.delete<unknown, void>(`/tables/${tableId}/buttons/${buttonId}`),
  toggle: (tableId: number | string, buttonId: number, enabled: boolean) =>
    http.put<unknown, TableButton>(`/tables/${tableId}/buttons/${buttonId}/enabled`, null, {
      params: { enabled }
    })
}

// ===== 数据预览 =====
export const PreviewApi = {
  preview: (id: number | string) => http.get<unknown, Record<string, any>>(`/preview/${id}`)
}

// ===== 表单字段（FORM）=====
export const FieldApi = {
  listByTable: async (tableId: number | string) => (await TableApi.detail(tableId)).formFields,
  add: (tableId: number | string, payload: FormField) =>
    http.post<unknown, FormField>(`/tables/${tableId}/fields`, payload),
  update: (tableId: number | string, fieldId: number, payload: FormField) =>
    http.put<unknown, FormField>(`/tables/${tableId}/fields/${fieldId}`, payload),
  remove: (tableId: number | string, fieldId: number) =>
    http.delete<unknown, void>(`/tables/${tableId}/fields/${fieldId}`),
  move: (tableId: number | string, fieldId: number, direction: number) =>
    http.put<unknown, void>(`/tables/${tableId}/fields/${fieldId}/move`, null, { params: { direction } })
}

// ===== 子表映射 =====
export const SubTableApi = {
  listByTable: async (tableId: number | string) => (await TableApi.detail(tableId)).subTables,
  add: (tableId: number | string, payload: SubTableMapping) =>
    http.post<unknown, SubTableMapping>(`/tables/${tableId}/subtables`, payload),
  update: (tableId: number | string, subId: number, payload: SubTableMapping) =>
    http.put<unknown, SubTableMapping>(`/tables/${tableId}/subtables/${subId}`, payload),
  remove: (tableId: number | string, subId: number) =>
    http.delete<unknown, void>(`/tables/${tableId}/subtables/${subId}`)
}

// ===== 表单填写预览 =====
export const FormPreviewApi = {
  preview: (id: number | string) => http.get<unknown, Record<string, any>>(`/form-preview/${id}`)
}
