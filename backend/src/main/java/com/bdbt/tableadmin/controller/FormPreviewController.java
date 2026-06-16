package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.service.FormPreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 表单填写预览接口：根据表单元数据动态返回可填写表单结构。
 */
@RestController
@RequestMapping("/api/form-preview")
public class FormPreviewController {

    @Autowired
    private FormPreviewService service;

    @GetMapping("/{tableId}")
    public ApiResponse<Map<String, Object>> preview(@PathVariable Long tableId) {
        return ApiResponse.ok(service.preview(tableId));
    }
}
