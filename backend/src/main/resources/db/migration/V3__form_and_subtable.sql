-- =====================================================
-- V3: 表单与子表支持 - page_type / form_field / sub_table_mapping
-- =====================================================

-- 1) 表格配置增加页面类型：GRID 表格 / FORM 表单
ALTER TABLE table_config ADD COLUMN page_type VARCHAR(16) NOT NULL DEFAULT 'GRID';
UPDATE table_config SET page_type = 'GRID' WHERE id IN (1, 2);

-- 2) 表单字段表（对应原型 _2 字段配置）
CREATE TABLE form_field (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_config_id BIGINT       NOT NULL,
    sort_order      INT          NOT NULL DEFAULT 0,
    group_name      VARCHAR(50),
    field_label     VARCHAR(100) NOT NULL,
    field_code      VARCHAR(64)  NOT NULL,
    control_type    VARCHAR(24)  NOT NULL DEFAULT 'TEXT',
    layout_weight   INT          NOT NULL DEFAULT 1,
    required        BOOLEAN      NOT NULL DEFAULT FALSE,
    visible         BOOLEAN      NOT NULL DEFAULT TRUE,
    read_only       BOOLEAN      NOT NULL DEFAULT FALSE,
    default_value   VARCHAR(200),
    placeholder     VARCHAR(200),
    options         VARCHAR(500),
    ref_entity      VARCHAR(64),
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_field_table FOREIGN KEY (table_config_id) REFERENCES table_config (id) ON DELETE CASCADE
);
CREATE INDEX idx_field_table ON form_field (table_config_id);

-- 3) 子表映射表（对应原型 _3 子表映射配置）
CREATE TABLE sub_table_mapping (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_config_id    BIGINT       NOT NULL,
    sort_order         INT          NOT NULL DEFAULT 0,
    sub_table_name     VARCHAR(100) NOT NULL,
    sub_table_code     VARCHAR(64)  NOT NULL,
    relation_field     VARCHAR(64),
    filter_condition   VARCHAR(500),
    show_header        BOOLEAN      NOT NULL DEFAULT TRUE,
    allow_add_row      BOOLEAN      NOT NULL DEFAULT TRUE,
    allow_batch_delete BOOLEAN      NOT NULL DEFAULT TRUE,
    height_mode        VARCHAR(16)  NOT NULL DEFAULT 'AUTO',
    fixed_height       INT,
    columns_json       VARCHAR(2000),
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subtable_table FOREIGN KEY (table_config_id) REFERENCES table_config (id) ON DELETE CASCADE
);
CREATE INDEX idx_subtable_table ON sub_table_mapping (table_config_id);

-- =====================================================
-- 4) 表单样例：汽运订单 - 编辑表单（对应原型 _2 / _10）
-- =====================================================
INSERT INTO table_config (id, page_type, table_name, table_code, data_source, layout_mode, status, description)
VALUES (3, 'FORM', '汽运订单编辑表单', 'FORM_TRANS_ORDER', 'PostgreSQL 生产集群', 'PURE_LIST', 'PUBLISHED',
        '汽运订单录入/编辑表单，覆盖文本/数字/下拉/单选/多选/日期/开关/评分/关联/子表等控件');

-- 表单字段：基础信息分组
INSERT INTO form_field (id, table_config_id, sort_order, group_name, field_label, field_code, control_type, layout_weight, required, visible, read_only, default_value, placeholder, options, ref_entity) VALUES
(401, 3, 1, '基础信息', '订单编号',   'order_no',    'TEXT',    1, TRUE,  TRUE, TRUE,  NULL,        '系统自动生成',    NULL, NULL),
(402, 3, 2, '基础信息', '客户名称',   'customer',    'REF',     1, TRUE,  TRUE, FALSE, NULL,        '选择客户',        NULL, 'T_CUSTOMER'),
(403, 3, 3, '基础信息', '订单日期',   'order_date',  'DATE',    1, TRUE,  TRUE, FALSE, NULL,        '选择日期',        NULL, NULL),
(404, 3, 4, '基础信息', '订单状态',   'status',      'SELECT',  1, FALSE, TRUE, FALSE, '待处理',    '请选择',          '待处理,处理中,已发货,已送达', NULL),
(405, 3, 5, '基础信息', '优先级',     'priority',    'RADIO',   2, FALSE, TRUE, FALSE, '中',        NULL,              '低,中,高,紧急', NULL),
(406, 3, 6, '基础信息', '承运商',     'carrier',     'REF',     1, FALSE, TRUE, FALSE, NULL,        '选择承运商',      NULL, 'T_CARRIER'),
(407, 3, 7, '基础信息', '运输标签',   'tags',        'CHECKBOX',2, FALSE, TRUE, FALSE, NULL,        NULL,              '易碎,加急,冷链,贵重', NULL),
(408, 3, 8, '基础信息', '总重量(kg)', 'total_weight','NUMBER',  1, TRUE,  TRUE, FALSE, '0',         '请输入数字',      NULL, NULL);

-- 表单字段：任务详情分组
INSERT INTO form_field (id, table_config_id, sort_order, group_name, field_label, field_code, control_type, layout_weight, required, visible, read_only, default_value, placeholder, options, ref_entity) VALUES
(411, 3, 9,  '任务详情', '任务描述',     'task_desc',   'TEXTAREA', 2, FALSE, TRUE, FALSE, NULL, '填写任务说明…',           NULL, NULL),
(412, 3, 10, '任务详情', '预约送达时间', 'deliver_time','DATETIME', 1, FALSE, TRUE, FALSE, NULL, '精确到时分',             NULL, NULL),
(413, 3, 11, '任务详情', '责任人',       'owner',       'TEXT',     1, FALSE, TRUE, FALSE, NULL, '责任人姓名',             NULL, NULL);

-- 表单字段：高级设置分组
INSERT INTO form_field (id, table_config_id, sort_order, group_name, field_label, field_code, control_type, layout_weight, required, visible, read_only, default_value, placeholder, options, ref_entity) VALUES
(421, 3, 12, '高级设置', '是否加急',   'urgent',    'SWITCH', 1, FALSE, TRUE, FALSE, 'false', NULL, NULL, NULL),
(422, 3, 13, '高级设置', '服务评分',   'rating',    'RATING', 1, FALSE, TRUE, FALSE, '3',     NULL, NULL, NULL),
(423, 3, 14, '高级设置', '备注',       'remarks',   'TEXT',   2, FALSE, TRUE, FALSE, NULL,    '可选', NULL, NULL);

-- 表单字段：明细分组 - 子表嵌入
INSERT INTO form_field (id, table_config_id, sort_order, group_name, field_label, field_code, control_type, layout_weight, required, visible, read_only, default_value, placeholder, options, ref_entity) VALUES
(431, 3, 15, '订单明细', '货物明细', 'order_items', 'SUBTABLE', 2, FALSE, TRUE, FALSE, NULL, NULL, NULL, NULL);

-- 表单按钮
INSERT INTO table_button (id, table_config_id, sort_order, name, code, icon, action_type, position, button_style, enabled, event_handler) VALUES
(331, 3, 1, '保存',     'btn_save',   'save',       'CUSTOM', 'HEADER', 'PRIMARY', TRUE, 'handleSubmit'),
(332, 3, 2, '重置',     'btn_reset',  'restart_alt','CUSTOM', 'HEADER', 'GHOST',   TRUE, 'resetForm'),
(333, 3, 3, '提交审批', 'btn_submit', 'send',       'CUSTOM', 'HEADER', 'PRIMARY', TRUE, 'submitToWorkflow');

-- 子表映射：订单明细（对应原型 _3），columns_json 为字段权限矩阵
INSERT INTO sub_table_mapping (id, table_config_id, sort_order, sub_table_name, sub_table_code, relation_field, filter_condition, show_header, allow_add_row, allow_batch_delete, height_mode, fixed_height, columns_json) VALUES
(501, 3, 1, '订单明细', 'TB_ORDER_ITEMS_001', 'order_id', 'is_deleted = 0', TRUE, TRUE, TRUE, 'FIXED', 240,
 '[{"name":"物料编码","key":"sku_id","show":true,"edit":true,"required":true},{"name":"物料名称","key":"sku_name","show":true,"edit":false,"required":false},{"name":"数量","key":"quantity","show":true,"edit":true,"required":true},{"name":"含税单价","key":"unit_price","show":true,"edit":true,"required":true},{"name":"金额合计","key":"total_amount","show":true,"edit":false,"required":false},{"name":"备注","key":"remarks","show":true,"edit":true,"required":false}]');

-- =====================================================
-- 5) 第二个表单样例：资产盘点表单（控件组合更简，演示差异）
-- =====================================================
INSERT INTO table_config (id, page_type, table_name, table_code, data_source, layout_mode, status, description)
VALUES (4, 'FORM', '资产盘点表单', 'FORM_ASSET_CHECK', '遗留 SAP 集成', 'PURE_LIST', 'DRAFT',
        '资产盘点录入表单，演示开关/评分/单选/日期等控件的另一种组合');

INSERT INTO form_field (id, table_config_id, sort_order, group_name, field_label, field_code, control_type, layout_weight, required, visible, read_only, default_value, placeholder, options, ref_entity) VALUES
(451, 4, 1, '盘点信息', '资产编号', 'asset_code',  'TEXT',    1, TRUE,  TRUE, TRUE,  NULL,  '扫码或手输',    NULL, NULL),
(452, 4, 2, '盘点信息', '设备名称', 'device_name', 'REF',     1, TRUE,  TRUE, FALSE, NULL,  '选择设备',      NULL, 'T_ASSET_MAIN'),
(453, 4, 3, '盘点信息', '盘点日期', 'check_date',  'DATE',    1, TRUE,  TRUE, FALSE, NULL,  '选择日期',      NULL, NULL),
(454, 4, 4, '盘点信息', '运行状态', 'run_status',  'RADIO',   2, TRUE,  TRUE, FALSE, '正常','请选择',        '正常,异常,维修中', NULL),
(461, 4, 5, '盘点结果', '是否需要维修', 'need_repair','SWITCH', 1, FALSE, TRUE, FALSE,'false',NULL,           NULL, NULL),
(462, 4, 6, '盘点结果', '完好度评分',   'condition',  'RATING',  1, FALSE, TRUE, FALSE,'4',   NULL,            NULL, NULL),
(463, 4, 7, '盘点结果', '问题类别',     'issue_type', 'CHECKBOX',2, FALSE, TRUE, FALSE,NULL,  '可多选',        '外观,电气,机械,其它', NULL),
(464, 4, 8, '盘点结果', '备注说明',     'remarks',    'TEXTAREA',2, FALSE, TRUE, FALSE,NULL,  '问题详细描述',  NULL, NULL);

INSERT INTO table_button (id, table_config_id, sort_order, name, code, icon, action_type, position, button_style, enabled, event_handler) VALUES
(341, 4, 1, '保存盘点', 'btn_save', 'save', 'CUSTOM', 'HEADER', 'PRIMARY', TRUE, 'handleSave');
