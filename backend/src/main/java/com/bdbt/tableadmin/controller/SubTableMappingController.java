package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.dto.SubTableMappingDto;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 子表映射配置接口（对应原型 _3「子表映射配置」）。
 */
@RestController
@RequestMapping("/api/tables/{tableId}/subtables")
public class SubTableMappingController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public ApiResponse<SubTableMappingDto> add(@PathVariable Long tableId, @Valid @RequestBody SubTableMappingDto dto) {
        return ApiResponse.ok(service.addSubTable(tableId, dto));
    }

    @PutMapping("/{subTableId}")
    public ApiResponse<SubTableMappingDto> update(@PathVariable Long tableId, @PathVariable Long subTableId,
                                                  @Valid @RequestBody SubTableMappingDto dto) {
        return ApiResponse.ok(service.updateSubTable(tableId, subTableId, dto));
    }

    @DeleteMapping("/{subTableId}")
    public ApiResponse<Void> delete(@PathVariable Long tableId, @PathVariable Long subTableId) {
        service.deleteSubTable(tableId, subTableId);
        return ApiResponse.ok();
    }
}
