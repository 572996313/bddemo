package com.bdbt.tableadmin.domain;

import com.bdbt.tableadmin.domain.common.BaseEntity;
import com.bdbt.tableadmin.domain.enums.FormFieldControlType;
import jakarta.persistence.*;

/**
 * 表单字段配置。
 * 对应原型 _2「表单配置维护 - 字段配置」：字段标签、编码、控件类型、必填、布局权重、默认值、占位符、关联实体、选项。
 */
@Entity
@Table(name = "form_field")
public class FormField extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_config_id", nullable = false)
    private TableConfig tableConfig;

    /** 排序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 字段分组（同分组字段聚集显示） */
    @Column(name = "group_name", length = 50)
    private String groupName;

    /** 字段标签（显示名） */
    @Column(name = "field_label", nullable = false, length = 100)
    private String fieldLabel;

    /** 字段编码 / 属性名，snake_case */
    @Column(name = "field_code", nullable = false, length = 64)
    private String fieldCode;

    /** 控件类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "control_type", nullable = false, length = 24)
    private FormFieldControlType controlType = FormFieldControlType.TEXT;

    /** 布局权重：1=半行，2=整行 */
    @Column(name = "layout_weight", nullable = false)
    private Integer layoutWeight = 1;

    /** 是否必填 */
    @Column(name = "required", nullable = false)
    private Boolean required = false;

    /** 是否可见 */
    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    /** 是否只读 */
    @Column(name = "read_only", nullable = false)
    private Boolean readOnly = false;

    /** 默认值 */
    @Column(name = "default_value", length = 200)
    private String defaultValue;

    /** 占位符 */
    @Column(name = "placeholder", length = 200)
    private String placeholder;

    /** 选项（SELECT/RADIO/CHECKBOX 用，逗号分隔） */
    @Column(name = "options", length = 500)
    private String options;

    /** 关联实体（REF 用，引用其它表格编码） */
    @Column(name = "ref_entity", length = 64)
    private String refEntity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TableConfig getTableConfig() { return tableConfig; }
    public void setTableConfig(TableConfig tableConfig) { this.tableConfig = tableConfig; }
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
