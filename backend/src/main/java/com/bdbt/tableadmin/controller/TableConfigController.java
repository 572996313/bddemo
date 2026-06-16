package com.bdbt.tableadmin.controller;

import com.bdbt.tableadmin.common.ApiResponse;
import com.bdbt.tableadmin.domain.enums.TableStatus;
import com.bdbt.tableadmin.dto.TableConfigDetail;
import com.bdbt.tableadmin.dto.TableConfigSummary;
import com.bdbt.tableadmin.service.TableConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表格配置（聚合根）接口。
 */
@RestController
@RequestMapping("/api/tables")
public class TableConfigController {

    @Autowired
    private TableConfigService service;

    /** 表格列表（摘要）。 */
    @GetMapping
    public ApiResponse<List<TableConfigSummary>> list() {
        return ApiResponse.ok(service.listSummaries());
    }

    /** 表格详情（含字段列、查询条件、按钮）。 */
    @GetMapping("/{id}")
    public ApiResponse<TableConfigDetail> detail(@PathVariable Long id) {
        return ApiResponse.ok(service.getDetail(id));
    }

    /** 新建表格。 */
    @PostMapping
    public ApiResponse<TableConfigDetail> create(@Valid @RequestBody TableConfigDetail req) {
        return ApiResponse.ok(service.create(req));
    }

    /** 更新表格基础信息。 */
    @PutMapping("/{id}")
    public ApiResponse<TableConfigDetail> updateBasic(@PathVariable Long id,
                                                      @Valid @RequestBody TableConfigDetail req) {
        return ApiResponse.ok(service.updateBasic(id, req));
    }

    /** 整体保存（含全部子配置的全量替换）。 */
    @PutMapping("/{id}/full")
    public ApiResponse<TableConfigDetail> saveFull(@PathVariable Long id,
                                                   @Valid @RequestBody TableConfigDetail req) {
        return ApiResponse.ok(service.saveDetail(id, req));
    }

    /** 删除表格（级联删除子配置）。 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok();
    }

    /** 更新表格发布状态。 */
    @PutMapping("/{id}/status")
    public ApiResponse<TableConfigDetail> updateStatus(@PathVariable Long id,
                                                       @RequestParam TableStatus status) {
        return ApiResponse.ok(service.updateStatus(id, status));
    }
}
