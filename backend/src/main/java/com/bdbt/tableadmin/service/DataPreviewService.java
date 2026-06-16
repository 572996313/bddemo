package com.bdbt.tableadmin.service;

import com.bdbt.tableadmin.common.BusinessException;
import com.bdbt.tableadmin.domain.ColumnConfig;
import com.bdbt.tableadmin.domain.TableButton;
import com.bdbt.tableadmin.domain.TableConfig;
import com.bdbt.tableadmin.repository.TableConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 数据预览服务：根据表格的列与按钮元数据，动态生成示例数据行，
 * 用于「数据查询中心」实时渲染，体现元数据驱动的表格能力。
 */
@Service
public class DataPreviewService {

    private final TableConfigRepository tableConfigRepository;

    public DataPreviewService(TableConfigRepository tableConfigRepository) {
        this.tableConfigRepository = tableConfigRepository;
    }

    private static final List<String> CARRIERS = List.of("COSCO", "Maersk", "CMA CGM", "MSC", "中远海运");
    private static final List<String> CUSTOMERS = List.of("上海申通快递", "中通供应链", "顺丰速运", "德邦物流", "京东物流");
    private static final List<String> ROUTES = List.of("Shanghai → Hamburg", "Ningbo → Los Angeles", "Shenzhen → Rotterdam", "Qingdao → Singapore");
    private static final List<String> STATUSES = List.of("pending", "processing", "shipped", "delivered");
    private static final List<String> STATUS_ZH = List.of("进行中", "已完成", "待审核", "草稿");
    private static final List<String> DEVICES = List.of("高压离心泵 A-12", "工业控制器 PLC-V2", "光纤传感器 FX-01", "耐高温密封组件", "变频器 VFD-300");

    @Transactional(readOnly = true)
    public Map<String, Object> preview(Long tableId) {
        TableConfig table = tableConfigRepository.findById(tableId)
                .orElseThrow(() -> BusinessException.notFound("表格配置不存在：id=" + tableId));

        List<ColumnConfig> visibleColumns = table.getColumns().stream()
                .filter(c -> Boolean.TRUE.equals(c.getVisible()))
                .sorted(Comparator.comparingInt(ColumnConfig::getSortOrder))
                .toList();

        // 列定义映射为轻量结构（避免泄漏实体反向引用与时间戳）
        List<Map<String, Object>> columnDefs = new ArrayList<>();
        for (ColumnConfig c : visibleColumns) {
            Map<String, Object> col = new LinkedHashMap<>();
            col.put("dataKey", c.getDataKey());
            col.put("displayName", c.getDisplayName());
            col.put("width", c.getWidth());
            col.put("alignment", c.getAlignment());
            col.put("sortable", c.getSortable());
            columnDefs.add(col);
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (ColumnConfig col : visibleColumns) {
                row.put(col.getDataKey(), mockValue(col.getDataKey(), i));
            }
            rows.add(row);
        }

        List<String> headerActions = table.getButtons().stream()
                .filter(b -> Boolean.TRUE.equals(b.getEnabled()) && b.getPosition().name().equals("HEADER"))
                .sorted(Comparator.comparingInt(TableButton::getSortOrder))
                .map(TableButton::getName)
                .toList();
        List<String> rowActions = table.getButtons().stream()
                .filter(b -> Boolean.TRUE.equals(b.getEnabled()) && b.getPosition().name().equals("ROW"))
                .sorted(Comparator.comparingInt(TableButton::getSortOrder))
                .map(TableButton::getName)
                .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tableName", table.getTableName());
        result.put("tableCode", table.getTableCode());
        result.put("layoutMode", table.getLayoutMode());
        result.put("columns", columnDefs);
        result.put("headerActions", headerActions);
        result.put("rowActions", rowActions);
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    /** 根据字段名约定生成相对合理的示例值。 */
    private Object mockValue(String dataKey, int seq) {
        String k = dataKey.toLowerCase();
        if (k.contains("id")) return "#ORD-2023-" + String.format("%03d", seq);
        if (k.contains("customer")) return CUSTOMERS.get(seq % CUSTOMERS.size());
        if (k.contains("carrier")) return CARRIERS.get(seq % CARRIERS.size());
        if (k.contains("route")) return ROUTES.get(seq % ROUTES.size());
        if (k.contains("status")) return seq % 2 == 0
                ? STATUS_ZH.get(seq % STATUS_ZH.size())
                : STATUSES.get(seq % STATUSES.size());
        if (k.contains("date") || k.contains("ts")) return String.format("2023-11-%02d", seq + 10);
        if (k.contains("weight")) return 1200 + seq * 350;
        if (k.contains("amount")) return 5600 + seq * 1800 + ".00";
        if (k.contains("device") || k.contains("name")) return DEVICES.get(seq % DEVICES.size());
        if (k.contains("workshop")) return "车间-" + ((seq % 3) + 1);
        return dataKey + "_" + seq;
    }
}
