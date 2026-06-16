package com.bdbt.tableadmin.domain.enums;

/**
 * 表格布局模式（对应原型 _6 基础信息维护中的布局选择）。
 */
public enum LayoutMode {
    /** 纯主表列表 */
    PURE_LIST,
    /** 侧边抽屉明细 */
    SIDE_DRAWER,
    /** 主从表联动 */
    MASTER_DETAIL,
    /** 增强查询布局 */
    ENHANCED_QUERY
}
