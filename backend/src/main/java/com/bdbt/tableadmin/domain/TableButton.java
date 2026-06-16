package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.ActionType;
import com.bdbt.tableadmin.domain.enums.ButtonPosition;
import com.bdbt.tableadmin.domain.enums.ButtonStyle;
import jakarta.persistence.*;

/**
 * 表格按钮（表格功能）。
 * 对应原型 _1「按钮功能管理」：按钮名称、编码、图标、操作类型、位置、样式、事件处理、是否启用。
 */
@Entity
@Table(name = "table_button")
public class TableButton extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_config_id", nullable = false)
    private TableConfig tableConfig;

    /** 排序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 按钮名称 */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /** 按钮编码，如 btn_create_order */
    @Column(name = "code", nullable = false, length = 64)
    private String code;

    /** 图标标识（Material Symbols 名称），如 add_circle */
    @Column(name = "icon", length = 64)
    private String icon;

    /** 操作类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 16)
    private ActionType actionType;

    /** 位置：表头 / 行内 */
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, length = 16)
    private ButtonPosition position = ButtonPosition.HEADER;

    /** 样式 */
    @Enumerated(EnumType.STRING)
    @Column(name = "button_style", nullable = false, length = 16)
    private ButtonStyle buttonStyle = ButtonStyle.PRIMARY;

    /** 是否启用 */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /** 事件处理函数名，如 handleSubmit */
    @Column(name = "event_handler", length = 100)
    private String eventHandler;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TableConfig getTableConfig() { return tableConfig; }
    public void setTableConfig(TableConfig tableConfig) { this.tableConfig = tableConfig; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public ButtonPosition getPosition() { return position; }
    public void setPosition(ButtonPosition position) { this.position = position; }
    public ButtonStyle getButtonStyle() { return buttonStyle; }
    public void setButtonStyle(ButtonStyle buttonStyle) { this.buttonStyle = buttonStyle; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getEventHandler() { return eventHandler; }
    public void setEventHandler(String eventHandler) { this.eventHandler = eventHandler; }
}
