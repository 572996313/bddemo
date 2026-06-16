package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.SubTableHeightMode;
import jakarta.persistence.*;

/**
 * 子表映射配置。
 * 对应原型 _3「子表映射配置」：关联表编码、外键关联字段、过滤条件、显示规则、表高模式。
 */
@Entity
@Table(name = "sub_table_mapping")
public class SubTableMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_config_id", nullable = false)
    private TableConfig tableConfig;

    /** 排序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 子表名称（显示） */
    @Column(name = "sub_table_name", nullable = false, length = 100)
    private String subTableName;

    /** 关联表格编码 */
    @Column(name = "sub_table_code", nullable = false, length = 64)
    private String subTableCode;

    /** 外键关联字段 */
    @Column(name = "relation_field", length = 64)
    private String relationField;

    /** 默认过滤条件（SQL where） */
    @Column(name = "filter_condition", length = 500)
    private String filterCondition;

    /** 显示表头 */
    @Column(name = "show_header", nullable = false)
    private Boolean showHeader = true;

    /** 允许新增行 */
    @Column(name = "allow_add_row", nullable = false)
    private Boolean allowAddRow = true;

    /** 允许批量删除 */
    @Column(name = "allow_batch_delete", nullable = false)
    private Boolean allowBatchDelete = true;

    /** 表高模式 */
    @Enumerated(EnumType.STRING)
    @Column(name = "height_mode", nullable = false, length = 16)
    private SubTableHeightMode heightMode = SubTableHeightMode.AUTO;

    /** 固定高度(px) */
    @Column(name = "fixed_height")
    private Integer fixedHeight;

    /** 子表列定义（JSON 字符串，简化字段权限） */
    @Column(name = "columns_json", length = 2000)
    private String columnsJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TableConfig getTableConfig() { return tableConfig; }
    public void setTableConfig(TableConfig tableConfig) { this.tableConfig = tableConfig; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getSubTableName() { return subTableName; }
    public void setSubTableName(String subTableName) { this.subTableName = subTableName; }
    public String getSubTableCode() { return subTableCode; }
    public void setSubTableCode(String subTableCode) { this.subTableCode = subTableCode; }
    public String getRelationField() { return relationField; }
    public void setRelationField(String relationField) { this.relationField = relationField; }
    public String getFilterCondition() { return filterCondition; }
    public void setFilterCondition(String filterCondition) { this.filterCondition = filterCondition; }
    public Boolean getShowHeader() { return showHeader; }
    public void setShowHeader(Boolean showHeader) { this.showHeader = showHeader; }
    public Boolean getAllowAddRow() { return allowAddRow; }
    public void setAllowAddRow(Boolean allowAddRow) { this.allowAddRow = allowAddRow; }
    public Boolean getAllowBatchDelete() { return allowBatchDelete; }
    public void setAllowBatchDelete(Boolean allowBatchDelete) { this.allowBatchDelete = allowBatchDelete; }
    public SubTableHeightMode getHeightMode() { return heightMode; }
    public void setHeightMode(SubTableHeightMode heightMode) { this.heightMode = heightMode; }
    public Integer getFixedHeight() { return fixedHeight; }
    public void setFixedHeight(Integer fixedHeight) { this.fixedHeight = fixedHeight; }
    public String getColumnsJson() { return columnsJson; }
    public void setColumnsJson(String columnsJson) { this.columnsJson = columnsJson; }
}
