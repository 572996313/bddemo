package com.bdbt.tableadmin.domain.enums;

/**
 * 表单字段控件类型（对应原型 _2 字段配置 / _10 表单填写）。
 */
public enum FormFieldControlType {
    /** 单行文本 */
    TEXT,
    /** 数字 */
    NUMBER,
    /** 多行文本 */
    TEXTAREA,
    /** 下拉选择 */
    SELECT,
    /** 单选 */
    RADIO,
    /** 多选 */
    CHECKBOX,
    /** 日期 */
    DATE,
    /** 日期时间 */
    DATETIME,
    /** 开关 */
    SWITCH,
    /** 评分 */
    RATING,
    /** 关联选择（从其它表格选） */
    REF,
    /** 子表（嵌入可编辑明细行） */
    SUBTABLE
}
