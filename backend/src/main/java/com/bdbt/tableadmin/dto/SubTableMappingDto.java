package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.SubTableHeightMode;
import jakarta.validation.constraints.NotBlank;

public class SubTableMappingDto {
    private Long id;
    private Integer sortOrder;

    @NotBlank(message = "子表名称不能为空")
    private String subTableName;

    @NotBlank(message = "关联表格编码不能为空")
    private String subTableCode;

    private String relationField;
    private String filterCondition;
    private Boolean showHeader = true;
    private Boolean allowAddRow = true;
    private Boolean allowBatchDelete = true;
    private SubTableHeightMode heightMode = SubTableHeightMode.AUTO;
    private Integer fixedHeight;
    private String columnsJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
