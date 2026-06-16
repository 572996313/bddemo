package com.bdbt.tableadmin.domain.enums;

/** 查询匹配方式（对应原型 _5）。 */
public enum MatchMode {
    /** 精确匹配 */
    EXACT,
    /** 模糊匹配 */
    FUZZY,
    /** IN 多值匹配 */
    IN,
    /** BETWEEN 区间 */
    BETWEEN
}
