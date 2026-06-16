package com.bdbt.tableadmin.service;

import com.bdbt.tableadmin.common.BusinessException;
import com.bdbt.tableadmin.domain.FormField;
import com.bdbt.tableadmin.domain.SubTableMapping;
import com.bdbt.tableadmin.domain.TableButton;
import com.bdbt.tableadmin.domain.TableConfig;
import com.bdbt.tableadmin.repository.TableConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 表单预览服务：根据表单元数据动态生成可填写表单结构（分组 + 字段控件 + 默认值 + 选项），
 * 以及子表明细行（由 columnsJson 驱动），用于「表单填写预览」页（原型 _10）。
 */
@Service
public class FormPreviewService {

    private final TableConfigRepository tableConfigRepository;

    public FormPreviewService(TableConfigRepository tableConfigRepository) {
        this.tableConfigRepository = tableConfigRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> preview(Long tableId) {
        TableConfig table = tableConfigRepository.findById(tableId)
                .orElseThrow(() -> BusinessException.notFound("表格配置不存在：id=" + tableId));

        // 1) 按分组聚合字段，保留排序
        LinkedHashMap<String, List<Map<String, Object>>> grouped = new LinkedHashMap<>();
        for (FormField f : table.getFormFields()) {
            if (Boolean.FALSE.equals(f.getVisible())) continue;
            String group = f.getGroupName() != null && !f.getGroupName().isBlank()
                    ? f.getGroupName() : "默认分组";
            grouped.computeIfAbsent(group, k -> new ArrayList<>()).add(fieldView(f));
        }
        List<Map<String, Object>> groups = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> e : grouped.entrySet()) {
            Map<String, Object> g = new LinkedHashMap<>();
            g.put("name", e.getKey());
            g.put("fields", e.getValue());
            groups.add(g);
        }

        // 2) 子表明细
        List<Map<String, Object>> subTables = new ArrayList<>();
        for (SubTableMapping s : table.getSubTables()) {
            subTables.add(subTableView(s));
        }

        // 3) 表单按钮（表头）
        List<Map<String, Object>> actions = new ArrayList<>();
        for (TableButton b : table.getButtons()) {
            if (Boolean.FALSE.equals(b.getEnabled())) continue;
            Map<String, Object> a = new LinkedHashMap<>();
            a.put("name", b.getName());
            a.put("code", b.getCode());
            a.put("icon", b.getIcon());
            a.put("style", b.getButtonStyle());
            a.put("handler", b.getEventHandler());
            actions.add(a);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tableName", table.getTableName());
        result.put("tableCode", table.getTableCode());
        result.put("pageType", table.getPageType());
        result.put("groups", groups);
        result.put("subTables", subTables);
        result.put("actions", actions);
        return result;
    }

    private Map<String, Object> fieldView(FormField f) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("id", f.getId());
        v.put("label", f.getFieldLabel());
        v.put("code", f.getFieldCode());
        v.put("controlType", f.getControlType());
        v.put("layoutWeight", f.getLayoutWeight());
        v.put("required", f.getRequired());
        v.put("readOnly", f.getReadOnly());
        v.put("value", defaultValue(f));
        v.put("placeholder", f.getPlaceholder());
        v.put("options", parseList(f.getOptions()));
        v.put("refEntity", f.getRefEntity());
        v.put("refOptions", mockRefOptions(f.getRefEntity()));
        return v;
    }

    private Object defaultValue(FormField f) {
        if (f.getDefaultValue() == null) {
            return switch (f.getControlType()) {
                case SWITCH -> false;
                case RATING -> 0;
                case CHECKBOX -> Collections.emptyList();
                default -> null;
            };
        }
        return switch (f.getControlType()) {
            case SWITCH -> parseBool(f.getDefaultValue());
            case RATING -> parseIntSafe(f.getDefaultValue(), 0);
            case CHECKBOX -> parseList(f.getDefaultValue());
            default -> f.getDefaultValue();
        };
    }

    private Map<String, Object> subTableView(SubTableMapping s) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("id", s.getId());
        v.put("name", s.getSubTableName());
        v.put("code", s.getSubTableCode());
        v.put("relationField", s.getRelationField());
        v.put("filterCondition", s.getFilterCondition());
        v.put("showHeader", s.getShowHeader());
        v.put("allowAddRow", s.getAllowAddRow());
        v.put("allowBatchDelete", s.getAllowBatchDelete());
        v.put("heightMode", s.getHeightMode());
        v.put("fixedHeight", s.getFixedHeight());

        // 解析字段权限矩阵
        List<Map<String, Object>> columns = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (Map<String, Object> col : parseColumns(s.getColumnsJson())) {
            if (Boolean.FALSE.equals(col.get("show"))) continue;
            columns.add(col);
            headers.add(String.valueOf(col.get("name")));
            keys.add(String.valueOf(col.get("key")));
        }
        v.put("columns", columns);
        v.put("headers", headers);
        v.put("keys", keys);

        // 生成示例明细行
        List<Map<String, Object>> rows = new ArrayList<>();
        String[] items = {"工业控制器 A-12", "光纤传感器 FX-01", "耐高温密封组件", "变频器 VFD-300"};
        for (int i = 0; i < items.length; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            int qty = (i + 1) * 10;
            int price = 1250 + i * 600;
            for (int k = 0; k < keys.size(); k++) {
                String key = keys.get(k);
                row.put(key, mockCell(key, items[i], i, qty, price));
            }
            rows.add(row);
        }
        v.put("rows", rows);
        return v;
    }

    private Object mockCell(String key, String item, int i, int qty, int price) {
        if (key.contains("sku_id")) return "SKU-" + (1001 + i);
        if (key.contains("name")) return item;
        if (key.contains("quantity")) return qty;
        if (key.contains("price")) return price;
        if (key.contains("amount") || key.contains("total")) return qty * price;
        if (key.contains("remark")) return i % 2 == 0 ? "加急" : "";
        return "";
    }

    private List<String> parseList(String s) {
        if (s == null || s.isBlank()) return Collections.emptyList();
        List<String> list = new ArrayList<>();
        for (String p : s.split(",")) {
            if (!p.isBlank()) list.add(p.trim());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseColumns(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        // 极简 JSON 数组解析（避免引入 JSON 库）：列对象为 {..},{..}
        List<Map<String, Object>> result = new ArrayList<>();
        int i = 0;
        while (i < json.length()) {
            int open = json.indexOf('{', i);
            if (open < 0) break;
            int close = json.indexOf('}', open);
            if (close < 0) break;
            String obj = json.substring(open + 1, close);
            Map<String, Object> col = new LinkedHashMap<>();
            for (String pair : obj.split(",")) {
                int colon = pair.indexOf(':');
                if (colon < 0) continue;
                String k = pair.substring(0, colon).replaceAll("\"", "").trim();
                String raw = pair.substring(colon + 1).replaceAll("\"", "").trim();
                if ("show".equals(k) || "edit".equals(k) || "required".equals(k)) {
                    col.put(k, parseBool(raw));
                } else {
                    col.put(k, raw);
                }
            }
            result.add(col);
            i = close + 1;
        }
        return result;
    }

    private List<String> mockRefOptions(String refEntity) {
        if (refEntity == null) return Collections.emptyList();
        if (refEntity.contains("CUSTOMER")) return List.of("上海申通快递", "中通供应链", "顺丰速运", "德邦物流");
        if (refEntity.contains("CARRIER")) return List.of("COSCO", "Maersk", "CMA CGM", "MSC", "中远海运");
        if (refEntity.contains("ASSET")) return List.of("高压离心泵 A-12", "工业控制器 PLC-V2", "光纤传感器 FX-01");
        return List.of("选项 A", "选项 B", "选项 C");
    }

    private boolean parseBool(String s) {
        return "true".equalsIgnoreCase(s) || "1".equals(s);
    }

    private int parseIntSafe(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
