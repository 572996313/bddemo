package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.dto.ColumnConfigDto;
import com.bdbt.tableadmin.dto.ReorderRequest;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字段列配置接口（对应原型 _4「字段列配置」）。
 */
@RestController
@RequestMapping("/api/tables/{tableId}/columns")
public class ColumnConfigController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public ApiResponse<ColumnConfigDto> add(@PathVariable Long tableId,
                                            @Valid @RequestBody ColumnConfigDto dto) {
        return ApiResponse.ok(service.addColumn(tableId, dto));
    }

    @PutMapping("/{columnId}")
    public ApiResponse<ColumnConfigDto> update(@PathVariable Long tableId,
                                               @PathVariable Long columnId,
                                               @Valid @RequestBody ColumnConfigDto dto) {
        return ApiResponse.ok(service.updateColumn(tableId, columnId, dto));
    }

    @DeleteMapping("/{columnId}")
    public ApiResponse<Void> delete(@PathVariable Long tableId, @PathVariable Long columnId) {
        service.deleteColumn(tableId, columnId);
        return ApiResponse.ok();
    }

    /** 批量重排序。 */
    @PutMapping("/reorder")
    public ApiResponse<Void> reorder(@PathVariable Long tableId,
                                     @Valid @RequestBody ReorderRequest req) {
        service.reorderColumns(tableId, req);
        return ApiResponse.ok();
    }
}
