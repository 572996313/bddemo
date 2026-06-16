// 与后端枚举对齐的领域类型

export type PageType = 'GRID' | 'FORM'
export type LayoutMode = 'PURE_LIST' | 'SIDE_DRAWER' | 'MASTER_DETAIL' | 'ENHANCED_QUERY'
export type TableStatus = 'DRAFT' | 'PUBLISHED'
export type Alignment = 'LEFT' | 'CENTER' | 'RIGHT'
export type ControlType = 'INPUT' | 'DROPDOWN' | 'MULTI_SELECT' | 'DATE_RANGE' | 'CASCADE'
export type MatchMode = 'EXACT' | 'FUZZY' | 'IN' | 'BETWEEN'
export type ActionType = 'ADD' | 'EDIT' | 'DELETE' | 'EXPORT' | 'IMPORT' | 'CUSTOM'
export type ButtonPosition = 'HEADER' | 'ROW'
export type ButtonStyle = 'PRIMARY' | 'GHOST' | 'DANGER' | 'LINK'

/** 表单字段控件类型 */
export type FormControlType =
  | 'TEXT' | 'NUMBER' | 'TEXTAREA' | 'SELECT' | 'RADIO' | 'CHECKBOX'
  | 'DATE' | 'DATETIME' | 'SWITCH' | 'RATING' | 'REF' | 'SUBTABLE'

export type SubTableHeightMode = 'AUTO' | 'FIXED'

export interface TableConfigSummary {
  id: number
  tableName: string
  tableCode: string
  dataSource: string
  layoutMode: LayoutMode
  pageType: PageType
  status: TableStatus
  description: string
  columnCount: number
  queryConditionCount: number
  buttonCount: number
  formFieldCount: number
  subTableCount: number
}

export interface ColumnConfig {
  id?: number
  sortOrder: number
  displayName: string
  dataKey: string
  width?: number | null
  alignment: Alignment
  visible: boolean
  sortable: boolean
}

export interface QueryCondition {
  id?: number
  sortOrder: number
  label: string
  fieldKey: string
  controlType: ControlType
  matchMode: MatchMode
  defaultValue?: string | null
  validationRule?: string | null
}

export interface TableButton {
  id?: number
  sortOrder: number
  name: string
  code: string
  icon?: string | null
  actionType: ActionType
  position: ButtonPosition
  buttonStyle: ButtonStyle
  enabled: boolean
  eventHandler?: string | null
}

export interface TableConfigDetail {
  id?: number
  tableName: string
  tableCode: string
  dataSource?: string
  layoutMode?: LayoutMode
  pageType?: PageType
  status?: TableStatus
  description?: string
  columns: ColumnConfig[]
  queryConditions: QueryCondition[]
  buttons: TableButton[]
  formFields: FormField[]
  subTables: SubTableMapping[]
}

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// ===== 表单字段 / 子表映射 =====
export interface FormField {
  id?: number
  sortOrder: number
  groupName?: string
  fieldLabel: string
  fieldCode: string
  controlType: FormControlType
  layoutWeight: number
  required: boolean
  visible: boolean
  readOnly: boolean
  defaultValue?: string | null
  placeholder?: string | null
  options?: string | null
  refEntity?: string | null
}

export interface SubTableMapping {
  id?: number
  sortOrder: number
  subTableName: string
  subTableCode: string
  relationField?: string | null
  filterCondition?: string | null
  showHeader: boolean
  allowAddRow: boolean
  allowBatchDelete: boolean
  heightMode: SubTableHeightMode
  fixedHeight?: number | null
  columnsJson?: string | null
}

// ===== 选项常量（供下拉使用） =====
export const LAYOUT_MODE_OPTIONS: { value: LayoutMode; label: string; desc: string }[] = [
  { value: 'PURE_LIST', label: '纯主表列表', desc: '仅展示主表数据' },
  { value: 'SIDE_DRAWER', label: '侧边抽屉明细', desc: '点击行弹出右侧抽屉' },
  { value: 'MASTER_DETAIL', label: '主从表联动', desc: '主表选中联动下方明细' },
  { value: 'ENHANCED_QUERY', label: '增强查询布局', desc: '突出查询条件的布局' }
]

export const CONTROL_TYPE_OPTIONS: { value: ControlType; label: string }[] = [
  { value: 'INPUT', label: '输入框' },
  { value: 'DROPDOWN', label: '下拉选择' },
  { value: 'MULTI_SELECT', label: '多选' },
  { value: 'DATE_RANGE', label: '日期范围' },
  { value: 'CASCADE', label: '级联' }
]

export const MATCH_MODE_OPTIONS: { value: MatchMode; label: string }[] = [
  { value: 'EXACT', label: '精确匹配' },
  { value: 'FUZZY', label: '模糊匹配' },
  { value: 'IN', label: 'IN 多值' },
  { value: 'BETWEEN', label: '区间 Between' }
]

export const ACTION_TYPE_OPTIONS: { value: ActionType; label: string; color: string }[] = [
  { value: 'ADD', label: '添加', color: 'blue' },
  { value: 'EDIT', label: '编辑', color: 'gray' },
  { value: 'DELETE', label: '删除', color: 'red' },
  { value: 'EXPORT', label: '导出', color: 'green' },
  { value: 'IMPORT', label: '导入', color: 'amber' },
  { value: 'CUSTOM', label: '自定义', color: 'purple' }
]

export const BUTTON_STYLE_OPTIONS: { value: ButtonStyle; label: string }[] = [
  { value: 'PRIMARY', label: '主按钮' },
  { value: 'GHOST', label: '幽灵按钮' },
  { value: 'DANGER', label: '危险按钮' },
  { value: 'LINK', label: '链接文字' }
]

export const ALIGNMENT_OPTIONS: { value: Alignment; label: string }[] = [
  { value: 'LEFT', label: '左对齐' },
  { value: 'CENTER', label: '居中' },
  { value: 'RIGHT', label: '右对齐' }
]

// ===== 表单控件 =====
export const PAGE_TYPE_OPTIONS: { value: PageType; label: string; icon: string; desc: string }[] = [
  { value: 'GRID', label: '表格 / 列表', icon: 'table_view', desc: '数据列表 + 查询 + 操作按钮' },
  { value: 'FORM', label: '表单', icon: 'edit_document', desc: '字段录入 + 分组 + 子表' }
]

export const FORM_CONTROL_OPTIONS: { value: FormControlType; label: string; icon: string }[] = [
  { value: 'TEXT', label: '单行文本', icon: 'text_fields' },
  { value: 'NUMBER', label: '数字', icon: 'pin' },
  { value: 'TEXTAREA', label: '多行文本', icon: 'notes' },
  { value: 'SELECT', label: '下拉选择', icon: 'arrow_drop_down_circle' },
  { value: 'RADIO', label: '单选', icon: 'radio_button_checked' },
  { value: 'CHECKBOX', label: '多选', icon: 'check_box' },
  { value: 'DATE', label: '日期', icon: 'calendar_today' },
  { value: 'DATETIME', label: '日期时间', icon: 'schedule' },
  { value: 'SWITCH', label: '开关', icon: 'toggle_on' },
  { value: 'RATING', label: '评分', icon: 'star' },
  { value: 'REF', label: '关联选择', icon: 'link' },
  { value: 'SUBTABLE', label: '子表', icon: 'table_rows' }
]

export const SUBTABLE_HEIGHT_OPTIONS: { value: SubTableHeightMode; label: string }[] = [
  { value: 'AUTO', label: '自适应' },
  { value: 'FIXED', label: '固定高度' }
]
