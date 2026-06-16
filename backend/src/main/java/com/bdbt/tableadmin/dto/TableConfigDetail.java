package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.LayoutMode;
import com.bdbt.tableadmin.domain.enums.PageType;
import com.bdbt.tableadmin.domain.enums.TableStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 表格配置详情（含全部子表配置）。
 */
public class TableConfigDetail {
    private Long id;

    @NotBlank(message = "表格名称不能为空")
    private String tableName;

    @NotBlank(message = "唯一编码不能为空")
    private String tableCode;

    private String dataSource;
    private LayoutMode layoutMode;
    private PageType pageType;
    private TableStatus status;
    private String description;

    private List<ColumnConfigDto> columns;
    private List<QueryConditionDto> queryConditions;
    private List<TableButtonDto> buttons;
    private List<FormFieldDto> formFields;
    private List<SubTableMappingDto> subTables;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public LayoutMode getLayoutMode() { return layoutMode; }
    public void setLayoutMode(LayoutMode layoutMode) { this.layoutMode = layoutMode; }
    public PageType getPageType() { return pageType; }
    public void setPageType(PageType pageType) { this.pageType = pageType; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ColumnConfigDto> getColumns() { return columns; }
    public void setColumns(List<ColumnConfigDto> columns) { this.columns = columns; }
    public List<QueryConditionDto> getQueryConditions() { return queryConditions; }
    public void setQueryConditions(List<QueryConditionDto> queryConditions) { this.queryConditions = queryConditions; }
    public List<TableButtonDto> getButtons() { return buttons; }
    public void setButtons(List<TableButtonDto> buttons) { this.buttons = buttons; }
    public List<FormFieldDto> getFormFields() { return formFields; }
    public void setFormFields(List<FormFieldDto> formFields) { this.formFields = formFields; }
    public List<SubTableMappingDto> getSubTables() { return subTables; }
    public void setSubTables(List<SubTableMappingDto> subTables) { this.subTables = subTables; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
