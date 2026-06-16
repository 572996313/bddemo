package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.Alignment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ColumnConfigDto {
    private Long id;

    @NotNull(message = "排序号不能为空")
    private Integer sortOrder;

    @NotBlank(message = "列显示名不能为空")
    private String displayName;

    @NotBlank(message = "数据键不能为空")
    private String dataKey;

    private Integer width;
    private Alignment alignment = Alignment.LEFT;
    private Boolean visible = true;
    private Boolean sortable = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
