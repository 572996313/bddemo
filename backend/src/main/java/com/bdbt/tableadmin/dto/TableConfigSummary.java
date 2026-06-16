package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.LayoutMode;
import com.bdbt.tableadmin.domain.enums.PageType;
import com.bdbt.tableadmin.domain.enums.TableStatus;

/**
 * 表格配置摘要（列表页用）。
 */
public class TableConfigSummary {
    private Long id;
    private String tableName;
    private String tableCode;
    private String dataSource;
    private LayoutMode layoutMode;
    private PageType pageType;
    private TableStatus status;
    private String description;
    private int columnCount;
    private int queryConditionCount;
    private int buttonCount;
    private int formFieldCount;
    private int subTableCount;

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
    public int getColumnCount() { return columnCount; }
    public void setColumnCount(int columnCount) { this.columnCount = columnCount; }
    public int getQueryConditionCount() { return queryConditionCount; }
    public void setQueryConditionCount(int queryConditionCount) { this.queryConditionCount = queryConditionCount; }
    public int getButtonCount() { return buttonCount; }
    public void setButtonCount(int buttonCount) { this.buttonCount = buttonCount; }
    public int getFormFieldCount() { return formFieldCount; }
    public void setFormFieldCount(int formFieldCount) { this.formFieldCount = formFieldCount; }
    public int getSubTableCount() { return subTableCount; }
    public void setSubTableCount(int subTableCount) { this.subTableCount = subTableCount; }
}
