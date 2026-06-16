-- =====================================================
-- V2: 样例数据 - 对应原型 _1/_2/_4/_5/_6/_7 的配置
-- =====================================================

-- ---------- 表格1：资产主表（对应 _4/_5/_6 OpsFlow） ----------
INSERT INTO table_config (id, table_name, table_code, data_source, layout_mode, status, description)
VALUES (1, '资产主表', 'T_ASSET_MAIN', 'PostgreSQL 生产集群', 'SIDE_DRAWER', 'PUBLISHED',
        '资产台账主数据表，配置列、查询条件与操作按钮');

-- 字段列配置（_4）
INSERT INTO column_config (id, table_config_id, sort_order, display_name, data_key, width, alignment, visible, sortable) VALUES
(101, 1, 1, '资产编号',   'asset_id',     120, 'LEFT',   TRUE,  TRUE),
(102, 1, 2, '设备名称',   'device_name',  240, 'LEFT',   TRUE,  FALSE),
(103, 1, 3, '维护状态',   'maint_status', 140, 'CENTER', TRUE,  TRUE),
(104, 1, 4, '安装日期',   'install_date', 180, 'LEFT',   TRUE,  TRUE),
(105, 1, 5, '最后校准时间','last_cal_ts',  200, 'RIGHT',  TRUE,  FALSE),
(106, 1, 6, '所属车间',   'workshop',     120, 'LEFT',   FALSE, FALSE);

-- 查询条件（_5）
INSERT INTO query_condition (id, table_config_id, sort_order, label, field_key, control_type, match_mode, default_value, validation_rule) VALUES
(201, 1, 1, '资产编号', 'asset_id',     'INPUT',     'FUZZY',  NULL,           NULL),
(202, 1, 2, '维护状态', 'maint_status', 'DROPDOWN',  'EXACT',  '运行中',       NULL),
(203, 1, 3, '安装日期', 'install_date', 'DATE_RANGE','BETWEEN','最近 30 天',   NULL);

-- 按钮（_1：表头 + 行内操作）
INSERT INTO table_button (id, table_config_id, sort_order, name, code, icon, action_type, position, button_style, enabled, event_handler) VALUES
(301, 1, 1, '添加资产',   'btn_create_asset', 'add_circle',   'ADD',    'HEADER', 'PRIMARY', TRUE, 'handleCreate'),
(302, 1, 2, '批量删除',   'btn_batch_del',    'delete_sweep', 'DELETE', 'HEADER', 'DANGER',  TRUE, 'handleBatchDelete'),
(303, 1, 3, '导出 Excel', 'btn_export_xls',   'ios_share',    'EXPORT', 'HEADER', 'GHOST',   TRUE, 'handleExport'),
(304, 1, 4, '编辑',       'row_edit',         'edit_square',  'EDIT',   'ROW',    'LINK',    TRUE, 'handleEdit'),
(305, 1, 5, '删除',       'row_delete',       'delete',       'DELETE', 'ROW',    'GHOST',   TRUE, 'handleDelete');

-- ---------- 表格2：汽运订单（对应 _2/_7 Maintenance Pro） ----------
INSERT INTO table_config (id, table_name, table_code, data_source, layout_mode, status, description)
VALUES (2, '汽运订单', 'T_TRANS_ORDER_MAIN', 'PostgreSQL 生产集群', 'MASTER_DETAIL', 'DRAFT',
        '汽运订单主表，含订单明细子表，主从联动布局');

INSERT INTO column_config (id, table_config_id, sort_order, display_name, data_key, width, alignment, visible, sortable) VALUES
(111, 2, 1, '订单 ID',   'order_id',     160, 'LEFT',   TRUE,  TRUE),
(112, 2, 2, '客户名称',   'customer',     180, 'LEFT',   TRUE,  FALSE),
(113, 2, 3, '承运商',     'carrier',      140, 'LEFT',   TRUE,  FALSE),
(114, 2, 4, '路线',       'route',        220, 'LEFT',   TRUE,  FALSE),
(115, 2, 5, '创建日期',   'created_date', 160, 'LEFT',   TRUE,  TRUE),
(116, 2, 6, '状态',       'status',       120, 'CENTER', TRUE,  TRUE),
(117, 2, 7, '总重量(kg)', 'total_weight', 140, 'RIGHT',  TRUE,  TRUE),
(118, 2, 8, '金额',       'amount',       140, 'RIGHT',  TRUE,  TRUE);

INSERT INTO query_condition (id, table_config_id, sort_order, label, field_key, control_type, match_mode, default_value, validation_rule) VALUES
(211, 2, 1, '订单编号', 'order_id',   'INPUT',     'FUZZY',   NULL,  NULL),
(212, 2, 2, '订单状态', 'status',     'DROPDOWN',  'EXACT',   '全部', NULL),
(213, 2, 3, '日期范围', 'created_date','DATE_RANGE','BETWEEN', NULL,  NULL),
(214, 2, 4, '客户名称', 'customer',   'INPUT',     'FUZZY',   NULL,  NULL);

INSERT INTO table_button (id, table_config_id, sort_order, name, code, icon, action_type, position, button_style, enabled, event_handler) VALUES
(311, 2, 1, '新增',     'btn_create', 'add',      'ADD',    'HEADER', 'PRIMARY', TRUE, 'handleCreate'),
(312, 2, 2, '保存',     'btn_save',   'save',     'CUSTOM', 'HEADER', 'PRIMARY', TRUE, 'handleSubmit'),
(313, 2, 3, '重置',     'btn_reset',  'restart_alt','CUSTOM','HEADER','GHOST',   TRUE, 'resetForm'),
(314, 2, 4, '提交审批', 'btn_submit', 'send',     'CUSTOM', 'HEADER', 'PRIMARY', TRUE, 'submitToWorkflow'),
(315, 2, 5, '导出',     'btn_export', 'ios_share','EXPORT', 'HEADER', 'GHOST',   TRUE, 'handleExport'),
(316, 2, 6, '详情',     'row_detail', 'visibility','CUSTOM','ROW',    'LINK',    TRUE, 'handleDetail');
