package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.service.DataPreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据查询中心预览接口：根据表格元数据动态返回示例数据。
 */
@RestController
@RequestMapping("/api/preview")
public class DataPreviewController {

    @Autowired
    private DataPreviewService previewService;

    @GetMapping("/{tableId}")
    public ApiResponse<Map<String, Object>> preview(@PathVariable Long tableId) {
        return ApiResponse.ok(previewService.preview(tableId));
    }
}
