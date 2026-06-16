package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.dto.QueryConditionDto;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 查询条件配置接口（对应原型 _5「查询条件配置」）。
 */
@RestController
@RequestMapping("/api/tables/{tableId}/queries")
public class QueryConditionController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public ApiResponse<QueryConditionDto> add(@PathVariable Long tableId,
                                              @Valid @RequestBody QueryConditionDto dto) {
        return ApiResponse.ok(service.addQuery(tableId, dto));
    }

    @PutMapping("/{queryId}")
    public ApiResponse<QueryConditionDto> update(@PathVariable Long tableId,
                                                 @PathVariable Long queryId,
                                                 @Valid @RequestBody QueryConditionDto dto) {
        return ApiResponse.ok(service.updateQuery(tableId, queryId, dto));
    }

    @DeleteMapping("/{queryId}")
    public ApiResponse<Void> delete(@PathVariable Long tableId, @PathVariable Long queryId) {
        service.deleteQuery(tableId, queryId);
        return ApiResponse.ok();
    }
}
