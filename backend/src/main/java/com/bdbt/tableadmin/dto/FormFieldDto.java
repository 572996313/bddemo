package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.FormFieldControlType;
import jakarta.validation.constraints.NotBlank;

public class FormFieldDto {
    private Long id;
    private Integer sortOrder;

    private String groupName;

    @NotBlank(message = "字段标签不能为空")
    private String fieldLabel;

    @NotBlank(message = "字段编码不能为空")
    private String fieldCode;

    private FormFieldControlType controlType = FormFieldControlType.TEXT;
    private Integer layoutWeight = 1;
    private Boolean required = false;
    private Boolean visible = true;
    private Boolean readOnly = false;
    private String defaultValue;
    private String placeholder;
    private String options;
    private String refEntity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getFieldLabel() { return fieldLabel; }
    public void setFieldLabel(String fieldLabel) { this.fieldLabel = fieldLabel; }
    public String getFieldCode() { return fieldCode; }
    public void setFieldCode(String fieldCode) { this.fieldCode = fieldCode; }
    public FormFieldControlType getControlType() { return controlType; }
    public void setControlType(FormFieldControlType controlType) { this.controlType = controlType; }
    public Integer getLayoutWeight() { return layoutWeight; }
    public void setLayoutWeight(Integer layoutWeight) { this.layoutWeight = layoutWeight; }
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }
    public Boolean getReadOnly() { return readOnly; }
    public void setReadOnly(Boolean readOnly) { this.readOnly = readOnly; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    public String getRefEntity() { return refEntity; }
    public void setRefEntity(String refEntity) { this.refEntity = refEntity; }
}
