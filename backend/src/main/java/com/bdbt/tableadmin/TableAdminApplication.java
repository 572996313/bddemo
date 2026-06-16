package com.bdbt.tableadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 表格与表格功能维护系统 - 启动类。
 * <p>
 * 元数据驱动的表格配置平台：通过配置「表格 / 字段列 / 查询条件 / 按钮」四类元数据，
 * 驱动前端动态渲染数据表格与其操作功能。
 */
@SpringBootApplication
public class TableAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableAdminApplication.class, args);
    }
}
