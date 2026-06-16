package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.LayoutMode;
import com.bdbt.tableadmin.domain.enums.PageType;
import com.bdbt.tableadmin.domain.enums.TableStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格配置（聚合根）。
 * 对应原型 _6「基础信息维护」：定义表格名称、唯一编码、数据源、布局模式等核心属性。
 * 通过 pageType 区分「表格(GRID)」与「表单(FORM)」两类页面。
 */
@Entity
@Table(name = "table_config", uniqueConstraints = @UniqueConstraint(name = "uk_table_code", columnNames = "table_code"))
public class TableConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 页面类型：表格 / 表单 */
    @Enumerated(EnumType.STRING)
    @Column(name = "page_type", nullable = false, length = 16)
    private PageType pageType = PageType.GRID;

    /** 表格名称（显示名），如「资产主表」 */
    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    /** 唯一编码，如 T_ASSET_MAIN */
    @Column(name = "table_code", nullable = false, length = 64)
    private String tableCode;

    /** 数据源 */
    @Column(name = "data_source", length = 100)
    private String dataSource;

    /** 布局模式 */
    @Enumerated(EnumType.STRING)
    @Column(name = "layout_mode", nullable = false, length = 32)
    private LayoutMode layoutMode;

    /** 状态 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private TableStatus status = TableStatus.DRAFT;

    /** 备注 */
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "tableConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<ColumnConfig> columns = new ArrayList<>();

    @OneToMany(mappedBy = "tableConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<QueryCondition> queryConditions = new ArrayList<>();

    @OneToMany(mappedBy = "tableConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<TableButton> buttons = new ArrayList<>();

    /** 表单字段（pageType=FORM 用） */
    @OneToMany(mappedBy = "tableConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<FormField> formFields = new ArrayList<>();

    /** 子表映射（表单内嵌 / 主从联动用） */
    @OneToMany(mappedBy = "tableConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<SubTableMapping> subTables = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PageType getPageType() { return pageType; }
    public void setPageType(PageType pageType) { this.pageType = pageType; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public LayoutMode getLayoutMode() { return layoutMode; }
    public void setLayoutMode(LayoutMode layoutMode) { this.layoutMode = layoutMode; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ColumnConfig> getColumns() { return columns; }
    public void setColumns(List<ColumnConfig> columns) { this.columns = columns; }
    public List<QueryCondition> getQueryConditions() { return queryConditions; }
    public void setQueryConditions(List<QueryCondition> queryConditions) { this.queryConditions = queryConditions; }
    public List<TableButton> getButtons() { return buttons; }
    public void setButtons(List<TableButton> buttons) { this.buttons = buttons; }
    public List<FormField> getFormFields() { return formFields; }
    public void setFormFields(List<FormField> formFields) { this.formFields = formFields; }
    public List<SubTableMapping> getSubTables() { return subTables; }
    public void setSubTables(List<SubTableMapping> subTables) { this.subTables = subTables; }
}
