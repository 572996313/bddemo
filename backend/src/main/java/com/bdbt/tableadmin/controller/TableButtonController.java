package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.dto.TableButtonDto;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 表格按钮（表格功能）接口（对应原型 _1「按钮功能管理」）。
 */
@RestController
@RequestMapping("/api/tables/{tableId}/buttons")
public class TableButtonController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public ApiResponse<TableButtonDto> add(@PathVariable Long tableId,
                                           @Valid @RequestBody TableButtonDto dto) {
        return ApiResponse.ok(service.addButton(tableId, dto));
    }

    @PutMapping("/{buttonId}")
    public ApiResponse<TableButtonDto> update(@PathVariable Long tableId,
                                              @PathVariable Long buttonId,
                                              @Valid @RequestBody TableButtonDto dto) {
        return ApiResponse.ok(service.updateButton(tableId, buttonId, dto));
    }

    @DeleteMapping("/{buttonId}")
    public ApiResponse<Void> delete(@PathVariable Long tableId, @PathVariable Long buttonId) {
        service.deleteButton(tableId, buttonId);
        return ApiResponse.ok();
    }

    /** 启用/停用按钮。 */
    @PutMapping("/{buttonId}/enabled")
    public ApiResponse<TableButtonDto> toggle(@PathVariable Long tableId,
                                              @PathVariable Long buttonId,
                                              @RequestParam boolean enabled) {
        return ApiResponse.ok(service.toggleButton(tableId, buttonId, enabled));
    }
}
