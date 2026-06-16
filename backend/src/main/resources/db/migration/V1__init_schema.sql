-- =====================================================
-- V1: 初始化建表 - 表格配置元数据模型
-- =====================================================

CREATE TABLE table_config (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name  VARCHAR(100)  NOT NULL,
    table_code  VARCHAR(64)   NOT NULL,
    data_source VARCHAR(100),
    layout_mode VARCHAR(32)   NOT NULL,
    status      VARCHAR(16)   NOT NULL DEFAULT 'DRAFT',
    description VARCHAR(500),
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_table_code UNIQUE (table_code)
);

CREATE TABLE column_config (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_config_id BIGINT      NOT NULL,
    sort_order     INT          NOT NULL DEFAULT 0,
    display_name   VARCHAR(100) NOT NULL,
    data_key       VARCHAR(64)  NOT NULL,
    width          INT,
    alignment      VARCHAR(16)  NOT NULL DEFAULT 'LEFT',
    visible        BOOLEAN      NOT NULL DEFAULT TRUE,
    sortable       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_column_table FOREIGN KEY (table_config_id) REFERENCES table_config (id) ON DELETE CASCADE
);

CREATE INDEX idx_column_table ON column_config (table_config_id);

CREATE TABLE query_condition (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_config_id BIGINT       NOT NULL,
    sort_order      INT          NOT NULL DEFAULT 0,
    label           VARCHAR(100) NOT NULL,
    field_key       VARCHAR(64)  NOT NULL,
    control_type    VARCHAR(24)  NOT NULL DEFAULT 'INPUT',
    match_mode      VARCHAR(16)  NOT NULL DEFAULT 'EXACT',
    default_value   VARCHAR(200),
    validation_rule VARCHAR(200),
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_query_table FOREIGN KEY (table_config_id) REFERENCES table_config (id) ON DELETE CASCADE
);

CREATE INDEX idx_query_table ON query_condition (table_config_id);

CREATE TABLE table_button (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_config_id BIGINT      NOT NULL,
    sort_order     INT          NOT NULL DEFAULT 0,
    name           VARCHAR(50)  NOT NULL,
    code           VARCHAR(64)  NOT NULL,
    icon           VARCHAR(64),
    action_type    VARCHAR(16)  NOT NULL,
    position       VARCHAR(16)  NOT NULL DEFAULT 'HEADER',
    button_style   VARCHAR(16)  NOT NULL DEFAULT 'PRIMARY',
    enabled        BOOLEAN      NOT NULL DEFAULT TRUE,
    event_handler  VARCHAR(100),
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_button_table FOREIGN KEY (table_config_id) REFERENCES table_config (id) ON DELETE CASCADE
);

CREATE INDEX idx_button_table ON table_button (table_config_id);
