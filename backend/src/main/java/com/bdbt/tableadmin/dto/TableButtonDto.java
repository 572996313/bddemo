package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.ActionType;
import com.bdbt.tableadmin.domain.enums.ButtonPosition;
import com.bdbt.tableadmin.domain.enums.ButtonStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TableButtonDto {
    private Long id;
    private Integer sortOrder;

    @NotBlank(message = "按钮名称不能为空")
    private String name;

    @NotBlank(message = "按钮编码不能为空")
    private String code;

    private String icon;

    @NotNull(message = "操作类型不能为空")
    private ActionType actionType;

    private ButtonPosition position = ButtonPosition.HEADER;
    private ButtonStyle buttonStyle = ButtonStyle.PRIMARY;
    private Boolean enabled = true;
    private String eventHandler;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
