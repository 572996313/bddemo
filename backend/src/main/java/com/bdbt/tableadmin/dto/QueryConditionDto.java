package com.bdbt.tableadmin.dto;

import com.bdbt.tableadmin.domain.enums.ControlType;
import com.bdbt.tableadmin.domain.enums.MatchMode;
import jakarta.validation.constraints.NotBlank;

public class QueryConditionDto {
    private Long id;
    private Integer sortOrder;

    @NotBlank(message = "查询标签不能为空")
    private String label;

    @NotBlank(message = "字段名不能为空")
    private String fieldKey;

    private ControlType controlType = ControlType.INPUT;
    private MatchMode matchMode = MatchMode.EXACT;
    private String defaultValue;
    private String validationRule;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFieldKey() { return fieldKey; }
    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }
    public ControlType getControlType() { return controlType; }
    public void setControlType(ControlType controlType) { this.controlType = controlType; }
    public MatchMode getMatchMode() { return matchMode; }
    public void setMatchMode(MatchMode matchMode) { this.matchMode = matchMode; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getValidationRule() { return validationRule; }
    public void setValidationRule(String validationRule) { this.validationRule = validationRule; }
}
