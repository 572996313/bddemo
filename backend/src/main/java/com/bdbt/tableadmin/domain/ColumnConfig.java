package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.Alignment;
import jakarta.persistence.*;

/**
 * 字段列配置。
 * 对应原型 _4「字段列配置」：列显示名、数据键、列宽、对齐、可见、可排序。
 */
@Entity
@Table(name = "column_config")
public class ColumnConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_config_id", nullable = false)
    private TableConfig tableConfig;

    /** 排序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 列显示名 */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /** 数据键（后端字段名，snake_case） */
    @Column(name = "data_key", nullable = false, length = 64)
    private String dataKey;

    /** 列宽(px) */
    @Column(name = "width")
    private Integer width;

    /** 对齐方式 */
    @Enumerated(EnumType.STRING)
    @Column(name = "alignment", nullable = false, length = 16)
    private Alignment alignment = Alignment.LEFT;

    /** 是否可见 */
    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    /** 是否可排序 */
    @Column(name = "sortable", nullable = false)
    private Boolean sortable = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TableConfig getTableConfig() { return tableConfig; }
    public void setTableConfig(TableConfig tableConfig) { this.tableConfig = tableConfig; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDataKey() { return dataKey; }
    public void setDataKey(String dataKey) { this.dataKey = dataKey; }
    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
    public Alignment getAlignment() { return alignment; }
    public void setAlignment(Alignment alignment) { this.alignment = alignment; }
    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }
    public Boolean getSortable() { return sortable; }
    public void setSortable(Boolean sortable) { this.sortable = sortable; }
}
