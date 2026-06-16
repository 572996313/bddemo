package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.dto.FormFieldDto;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 表单字段配置接口（对应原型 _2「字段配置」）。
 */
@RestController
@RequestMapping("/api/tables/{tableId}/fields")
public class FormFieldController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public ApiResponse<FormFieldDto> add(@PathVariable Long tableId, @Valid @RequestBody FormFieldDto dto) {
        return ApiResponse.ok(service.addField(tableId, dto));
    }

    @PutMapping("/{fieldId}")
    public ApiResponse<FormFieldDto> update(@PathVariable Long tableId, @PathVariable Long fieldId,
                                            @Valid @RequestBody FormFieldDto dto) {
        return ApiResponse.ok(service.updateField(tableId, fieldId, dto));
    }

    @DeleteMapping("/{fieldId}")
    public ApiResponse<Void> delete(@PathVariable Long tableId, @PathVariable Long fieldId) {
        service.deleteField(tableId, fieldId);
        return ApiResponse.ok();
    }

    /** 上移 / 下移：direction = -1 上移, +1 下移 */
    @PutMapping("/{fieldId}/move")
    public ApiResponse<Void> move(@PathVariable Long tableId, @PathVariable Long fieldId,
                                 @RequestParam int direction) {
        service.moveField(tableId, fieldId, direction);
        return ApiResponse.ok();
    }
}
