package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.ControlType;
import com.bdbt.tableadmin.domain.enums.MatchMode;
import jakarta.persistence.*;

/**
 * 查询条件配置。
 * 对应原型 _5「查询条件配置」：查询标签、字段名、控件类型、匹配方式、默认值、校验规则。
 */
@Entity
@Table(name = "query_condition")
public class QueryCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_config_id", nullable = false)
    private TableConfig tableConfig;

    /** 排序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 查询标签（显示名） */
    @Column(name = "label", nullable = false, length = 100)
    private String label;

    /** 字段名（后端 key） */
    @Column(name = "field_key", nullable = false, length = 64)
    private String fieldKey;

    /** 控件类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "control_type", nullable = false, length = 24)
    private ControlType controlType = ControlType.INPUT;

    /** 匹配方式 */
    @Enumerated(EnumType.STRING)
    @Column(name = "match_mode", nullable = false, length = 16)
    private MatchMode matchMode = MatchMode.EXACT;

    /** 默认值 */
    @Column(name = "default_value", length = 200)
    private String defaultValue;

    /** 校验规则 */
    @Column(name = "validation_rule", length = 200)
    private String validationRule;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TableConfig getTableConfig() { return tableConfig; }
    public void setTableConfig(TableConfig tableConfig) { this.tableConfig = tableConfig; }
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
