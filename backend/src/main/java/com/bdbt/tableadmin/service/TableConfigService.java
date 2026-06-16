package com.bdbt.tableadmin.service;

import com.bdbt.tableadmin.domain.*;
import com.bdbt.tableadmin.domain.enums.TableStatus;
import com.bdbt.tableadmin.dto.*;
import com.bdbt.tableadmin.repository.*;
import com.bdbt.tableadmin.common.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表格配置核心服务：管理表格聚合根及其子配置
 * （字段列、查询条件、按钮、表单字段、子表映射）。
 */
@Service
public class TableConfigService {

    private final TableConfigRepository tableConfigRepository;
    private final ColumnConfigRepository columnConfigRepository;
    private final QueryConditionRepository queryConditionRepository;
    private final TableButtonRepository tableButtonRepository;
    private final FormFieldRepository formFieldRepository;
    private final SubTableMappingRepository subTableMappingRepository;

    public TableConfigService(TableConfigRepository tableConfigRepository,
                              ColumnConfigRepository columnConfigRepository,
                              QueryConditionRepository queryConditionRepository,
                              TableButtonRepository tableButtonRepository,
                              FormFieldRepository formFieldRepository,
                              SubTableMappingRepository subTableMappingRepository) {
        this.tableConfigRepository = tableConfigRepository;
        this.columnConfigRepository = columnConfigRepository;
        this.queryConditionRepository = queryConditionRepository;
        this.tableButtonRepository = tableButtonRepository;
        this.formFieldRepository = formFieldRepository;
        this.subTableMappingRepository = subTableMappingRepository;
    }

    // ============== 表格（聚合根） ==============

    @Transactional(readOnly = true)
    public List<TableConfigSummary> listSummaries() {
        return tableConfigRepository.findAll().stream()
                .map(TableConfigService::toSummary)
                .sorted(Comparator.comparing(TableConfigSummary::getId))
                .toList();
    }

    @Transactional(readOnly = true)
    public TableConfigDetail getDetail(Long id) {
        TableConfig table = requireTable(id);
        return toDetail(table);
    }

    @Transactional
    public TableConfigDetail create(TableConfigDetail req) {
        if (req.getTableCode() != null && tableConfigRepository.existsByTableCode(req.getTableCode())) {
            throw BusinessException.badRequest("表格编码已存在：" + req.getTableCode());
        }
        TableConfig table = new TableConfig();
        applyBasic(req, table);
        if (table.getStatus() == null) {
            table.setStatus(TableStatus.DRAFT);
        }
        return toDetail(tableConfigRepository.save(table));
    }

    @Transactional
    public TableConfigDetail updateBasic(Long id, TableConfigDetail req) {
        TableConfig table = requireTable(id);
        // 校验编码唯一（排除自身）
        if (req.getTableCode() != null) {
            tableConfigRepository.findByTableCode(req.getTableCode())
                    .filter(other -> !Objects.equals(other.getId(), id))
                    .ifPresent(other -> {
                        throw BusinessException.badRequest("表格编码已存在：" + req.getTableCode());
                    });
        }
        applyBasic(req, table);
        return toDetail(tableConfigRepository.save(table));
    }

    @Transactional
    public void delete(Long id) {
        if (!tableConfigRepository.existsById(id)) {
            throw BusinessException.notFound("表格配置不存在：id=" + id);
        }
        tableConfigRepository.deleteById(id);
    }

    @Transactional
    public TableConfigDetail updateStatus(Long id, TableStatus status) {
        TableConfig table = requireTable(id);
        table.setStatus(status);
        return toDetail(tableConfigRepository.save(table));
    }

    /** 整体保存：用请求中的子配置列表全量替换现有子配置。 */
    @Transactional
    public TableConfigDetail saveDetail(Long id, TableConfigDetail req) {
        TableConfig table = requireTable(id);
        applyBasic(req, table);

        replaceColumns(table, req.getColumns());
        replaceQueries(table, req.getQueryConditions());
        replaceButtons(table, req.getButtons());

        return toDetail(tableConfigRepository.save(table));
    }

    /** 全量替换字段列：保留/更新请求中带 id 的，新增无 id 的，删除请求中缺失的。 */
    private void replaceColumns(TableConfig table, List<ColumnConfigDto> items) {
        if (items == null) return;
        Map<Long, ColumnConfig> existing = table.getColumns().stream()
                .collect(Collectors.toMap(ColumnConfig::getId, Function.identity()));
        List<ColumnConfig> result = new ArrayList<>();
        Set<Long> keep = new HashSet<>();
        int order = 1;
        for (ColumnConfigDto dto : items) {
            ColumnConfig col;
            if (dto.getId() != null && existing.containsKey(dto.getId())) {
                col = existing.get(dto.getId());
                keep.add(dto.getId());
            } else {
                col = new ColumnConfig();
                col.setTableConfig(table);
            }
            applyColumn(dto, col);
            col.setSortOrder(order++);
            result.add(col);
        }
        table.getColumns().clear();
        table.getColumns().addAll(result);
    }

    private void replaceQueries(TableConfig table, List<QueryConditionDto> items) {
        if (items == null) return;
        Map<Long, QueryCondition> existing = table.getQueryConditions().stream()
                .collect(Collectors.toMap(QueryCondition::getId, Function.identity()));
        List<QueryCondition> result = new ArrayList<>();
        int order = 1;
        for (QueryConditionDto dto : items) {
            QueryCondition q;
            if (dto.getId() != null && existing.containsKey(dto.getId())) {
                q = existing.get(dto.getId());
            } else {
                q = new QueryCondition();
                q.setTableConfig(table);
            }
            applyQuery(dto, q);
            q.setSortOrder(order++);
            result.add(q);
        }
        table.getQueryConditions().clear();
        table.getQueryConditions().addAll(result);
    }

    private void replaceButtons(TableConfig table, List<TableButtonDto> items) {
        if (items == null) return;
        Map<Long, TableButton> existing = table.getButtons().stream()
                .collect(Collectors.toMap(TableButton::getId, Function.identity()));
        List<TableButton> result = new ArrayList<>();
        int order = 1;
        for (TableButtonDto dto : items) {
            TableButton btn;
            if (dto.getId() != null && existing.containsKey(dto.getId())) {
                btn = existing.get(dto.getId());
            } else {
                btn = new TableButton();
                btn.setTableConfig(table);
            }
            applyButton(dto, btn);
            btn.setSortOrder(order++);
            result.add(btn);
        }
        table.getButtons().clear();
        table.getButtons().addAll(result);
    }

    // ============== 字段列 ==============

    @Transactional
    public ColumnConfigDto addColumn(Long tableId, ColumnConfigDto dto) {
        TableConfig table = requireTable(tableId);
        ColumnConfig col = new ColumnConfig();
        col.setTableConfig(table);
        applyColumn(dto, col);
        if (col.getSortOrder() == null) {
            col.setSortOrder(nextSort(table.getColumns()));
        }
        return toColumnDto(columnConfigRepository.save(col));
    }

    @Transactional
    public ColumnConfigDto updateColumn(Long tableId, Long columnId, ColumnConfigDto dto) {
        ColumnConfig col = requireColumn(tableId, columnId);
        applyColumn(dto, col);
        return toColumnDto(columnConfigRepository.save(col));
    }

    @Transactional
    public void deleteColumn(Long tableId, Long columnId) {
        ColumnConfig col = requireColumn(tableId, columnId);
        columnConfigRepository.delete(col);
    }

    @Transactional
    public void reorderColumns(Long tableId, ReorderRequest req) {
        TableConfig table = requireTable(tableId);
        Map<Long, Integer> orderMap = req.getItems().stream()
                .collect(Collectors.toMap(ReorderRequest.Item::getId, ReorderRequest.Item::getSortOrder, (a, b) -> b));
        for (ColumnConfig col : table.getColumns()) {
            Integer order = orderMap.get(col.getId());
            if (order != null) {
                col.setSortOrder(order);
            }
        }
        columnConfigRepository.saveAll(table.getColumns());
    }

    // ============== 查询条件 ==============

    @Transactional
    public QueryConditionDto addQuery(Long tableId, QueryConditionDto dto) {
        TableConfig table = requireTable(tableId);
        QueryCondition q = new QueryCondition();
        q.setTableConfig(table);
        applyQuery(dto, q);
        if (q.getSortOrder() == null) {
            q.setSortOrder(nextSort(table.getQueryConditions()));
        }
        return toQueryDto(queryConditionRepository.save(q));
    }

    @Transactional
    public QueryConditionDto updateQuery(Long tableId, Long queryId, QueryConditionDto dto) {
        QueryCondition q = requireQuery(tableId, queryId);
        applyQuery(dto, q);
        return toQueryDto(queryConditionRepository.save(q));
    }

    @Transactional
    public void deleteQuery(Long tableId, Long queryId) {
        QueryCondition q = requireQuery(tableId, queryId);
        queryConditionRepository.delete(q);
    }

    // ============== 按钮 ==============

    @Transactional
    public TableButtonDto addButton(Long tableId, TableButtonDto dto) {
        TableConfig table = requireTable(tableId);
        TableButton btn = new TableButton();
        btn.setTableConfig(table);
        applyButton(dto, btn);
        if (btn.getSortOrder() == null) {
            btn.setSortOrder(nextSort(table.getButtons()));
        }
        return toButtonDto(tableButtonRepository.save(btn));
    }

    @Transactional
    public TableButtonDto updateButton(Long tableId, Long buttonId, TableButtonDto dto) {
        TableButton btn = requireButton(tableId, buttonId);
        applyButton(dto, btn);
        return toButtonDto(tableButtonRepository.save(btn));
    }

    @Transactional
    public void deleteButton(Long tableId, Long buttonId) {
        TableButton btn = requireButton(tableId, buttonId);
        tableButtonRepository.delete(btn);
    }

    @Transactional
    public TableButtonDto toggleButton(Long tableId, Long buttonId, boolean enabled) {
        TableButton btn = requireButton(tableId, buttonId);
        btn.setEnabled(enabled);
        return toButtonDto(tableButtonRepository.save(btn));
    }

    // ============== 表单字段 ==============

    @Transactional
    public FormFieldDto addField(Long tableId, FormFieldDto dto) {
        TableConfig table = requireTable(tableId);
        FormField f = new FormField();
        f.setTableConfig(table);
        applyField(dto, f);
        if (f.getSortOrder() == null) {
            f.setSortOrder(nextSort(table.getFormFields()));
        }
        return toFieldDto(formFieldRepository.save(f));
    }

    @Transactional
    public FormFieldDto updateField(Long tableId, Long fieldId, FormFieldDto dto) {
        FormField f = requireField(tableId, fieldId);
        applyField(dto, f);
        return toFieldDto(formFieldRepository.save(f));
    }

    @Transactional
    public void deleteField(Long tableId, Long fieldId) {
        FormField f = requireField(tableId, fieldId);
        formFieldRepository.delete(f);
    }

    /** 调整字段排序：上移/下移到目标位置 */
    @Transactional
    public void moveField(Long tableId, Long fieldId, int direction) {
        TableConfig table = requireTable(tableId);
        List<FormField> fields = table.getFormFields();
        int idx = -1;
        for (int i = 0; i < fields.size(); i++) {
            if (Objects.equals(fields.get(i).getId(), fieldId)) { idx = i; break; }
        }
        int target = idx + direction;
        if (idx < 0 || target < 0 || target >= fields.size()) return;
        FormField cur = fields.get(idx);
        FormField next = fields.get(target);
        int tmp = cur.getSortOrder();
        cur.setSortOrder(next.getSortOrder());
        next.setSortOrder(tmp);
        formFieldRepository.saveAll(List.of(cur, next));
    }

    // ============== 子表映射 ==============

    @Transactional
    public SubTableMappingDto addSubTable(Long tableId, SubTableMappingDto dto) {
        TableConfig table = requireTable(tableId);
        SubTableMapping s = new SubTableMapping();
        s.setTableConfig(table);
        applySubTable(dto, s);
        if (s.getSortOrder() == null) {
            s.setSortOrder(nextSort(table.getSubTables()));
        }
        return toSubTableDto(subTableMappingRepository.save(s));
    }

    @Transactional
    public SubTableMappingDto updateSubTable(Long tableId, Long subTableId, SubTableMappingDto dto) {
        SubTableMapping s = requireSubTable(tableId, subTableId);
        applySubTable(dto, s);
        return toSubTableDto(subTableMappingRepository.save(s));
    }

    @Transactional
    public void deleteSubTable(Long tableId, Long subTableId) {
        SubTableMapping s = requireSubTable(tableId, subTableId);
        subTableMappingRepository.delete(s);
    }

    // ============== 私有：校验 / 映射 / 工具 ==============

    private TableConfig requireTable(Long id) {
        return tableConfigRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("表格配置不存在：id=" + id));
    }

    private ColumnConfig requireColumn(Long tableId, Long columnId) {
        return columnConfigRepository.findById(columnId)
                .filter(c -> Objects.equals(c.getTableConfig().getId(), tableId))
                .orElseThrow(() -> BusinessException.notFound("字段列不存在：id=" + columnId));
    }

    private QueryCondition requireQuery(Long tableId, Long queryId) {
        return queryConditionRepository.findById(queryId)
                .filter(q -> Objects.equals(q.getTableConfig().getId(), tableId))
                .orElseThrow(() -> BusinessException.notFound("查询条件不存在：id=" + queryId));
    }

    private TableButton requireButton(Long tableId, Long buttonId) {
        return tableButtonRepository.findById(buttonId)
                .filter(b -> Objects.equals(b.getTableConfig().getId(), tableId))
                .orElseThrow(() -> BusinessException.notFound("按钮不存在：id=" + buttonId));
    }

    private FormField requireField(Long tableId, Long fieldId) {
        return formFieldRepository.findById(fieldId)
                .filter(f -> Objects.equals(f.getTableConfig().getId(), tableId))
                .orElseThrow(() -> BusinessException.notFound("表单字段不存在：id=" + fieldId));
    }

    private SubTableMapping requireSubTable(Long tableId, Long subTableId) {
        return subTableMappingRepository.findById(subTableId)
                .filter(s -> Objects.equals(s.getTableConfig().getId(), tableId))
                .orElseThrow(() -> BusinessException.notFound("子表映射不存在：id=" + subTableId));
    }

    private void applyBasic(TableConfigDetail req, TableConfig table) {
        if (req.getTableName() != null) table.setTableName(req.getTableName());
        if (req.getTableCode() != null) table.setTableCode(req.getTableCode());
        if (req.getDataSource() != null) table.setDataSource(req.getDataSource());
        if (req.getLayoutMode() != null) table.setLayoutMode(req.getLayoutMode());
        if (req.getPageType() != null) table.setPageType(req.getPageType());
        if (req.getStatus() != null) table.setStatus(req.getStatus());
        if (req.getDescription() != null) table.setDescription(req.getDescription());
    }

    private void applyColumn(ColumnConfigDto dto, ColumnConfig col) {
        col.setDisplayName(dto.getDisplayName());
        col.setDataKey(dto.getDataKey());
        col.setWidth(dto.getWidth());
        col.setAlignment(dto.getAlignment());
        col.setVisible(dto.getVisible());
        col.setSortable(dto.getSortable());
        if (dto.getSortOrder() != null) col.setSortOrder(dto.getSortOrder());
    }

    private void applyQuery(QueryConditionDto dto, QueryCondition q) {
        q.setLabel(dto.getLabel());
        q.setFieldKey(dto.getFieldKey());
        q.setControlType(dto.getControlType());
        q.setMatchMode(dto.getMatchMode());
        q.setDefaultValue(dto.getDefaultValue());
        q.setValidationRule(dto.getValidationRule());
        if (dto.getSortOrder() != null) q.setSortOrder(dto.getSortOrder());
    }

    private void applyButton(TableButtonDto dto, TableButton btn) {
        btn.setName(dto.getName());
        btn.setCode(dto.getCode());
        btn.setIcon(dto.getIcon());
        btn.setActionType(dto.getActionType());
        btn.setPosition(dto.getPosition());
        btn.setButtonStyle(dto.getButtonStyle());
        btn.setEnabled(dto.getEnabled());
        btn.setEventHandler(dto.getEventHandler());
        if (dto.getSortOrder() != null) btn.setSortOrder(dto.getSortOrder());
    }

    private void applyField(FormFieldDto dto, FormField f) {
        f.setGroupName(dto.getGroupName());
        f.setFieldLabel(dto.getFieldLabel());
        f.setFieldCode(dto.getFieldCode());
        f.setControlType(dto.getControlType());
        f.setLayoutWeight(dto.getLayoutWeight());
        f.setRequired(dto.getRequired());
        f.setVisible(dto.getVisible());
        f.setReadOnly(dto.getReadOnly());
        f.setDefaultValue(dto.getDefaultValue());
        f.setPlaceholder(dto.getPlaceholder());
        f.setOptions(dto.getOptions());
        f.setRefEntity(dto.getRefEntity());
        if (dto.getSortOrder() != null) f.setSortOrder(dto.getSortOrder());
    }

    private void applySubTable(SubTableMappingDto dto, SubTableMapping s) {
        s.setSubTableName(dto.getSubTableName());
        s.setSubTableCode(dto.getSubTableCode());
        s.setRelationField(dto.getRelationField());
        s.setFilterCondition(dto.getFilterCondition());
        s.setShowHeader(dto.getShowHeader());
        s.setAllowAddRow(dto.getAllowAddRow());
        s.setAllowBatchDelete(dto.getAllowBatchDelete());
        s.setHeightMode(dto.getHeightMode());
        s.setFixedHeight(dto.getFixedHeight());
        s.setColumnsJson(dto.getColumnsJson());
        if (dto.getSortOrder() != null) s.setSortOrder(dto.getSortOrder());
    }

    /**
     * 全量替换子集合：保留请求中带 id 且存在的实体，新增不带 id 的实体，删除不在请求中的实体。
     */
    private int nextSort(List<?> children) {
        int max = 0;
        for (Object c : children) {
            Integer o = null;
            if (c instanceof ColumnConfig cc) o = cc.getSortOrder();
            else if (c instanceof QueryCondition qq) o = qq.getSortOrder();
            else if (c instanceof TableButton bb) o = bb.getSortOrder();
            else if (c instanceof FormField ff) o = ff.getSortOrder();
            else if (c instanceof SubTableMapping sm) o = sm.getSortOrder();
            if (o != null && o > max) max = o;
        }
        return max + 1;
    }

    // ============== 映射到 DTO ==============

    private static TableConfigSummary toSummary(TableConfig t) {
        TableConfigSummary s = new TableConfigSummary();
        s.setId(t.getId());
        s.setTableName(t.getTableName());
        s.setTableCode(t.getTableCode());
        s.setDataSource(t.getDataSource());
        s.setLayoutMode(t.getLayoutMode());
        s.setPageType(t.getPageType());
        s.setStatus(t.getStatus());
        s.setDescription(t.getDescription());
        s.setColumnCount(t.getColumns().size());
        s.setQueryConditionCount(t.getQueryConditions().size());
        s.setButtonCount(t.getButtons().size());
        s.setFormFieldCount(t.getFormFields().size());
        s.setSubTableCount(t.getSubTables().size());
        return s;
    }

    private static TableConfigDetail toDetail(TableConfig t) {
        TableConfigDetail d = new TableConfigDetail();
        d.setId(t.getId());
        d.setTableName(t.getTableName());
        d.setTableCode(t.getTableCode());
        d.setDataSource(t.getDataSource());
        d.setLayoutMode(t.getLayoutMode());
        d.setPageType(t.getPageType());
        d.setStatus(t.getStatus());
        d.setDescription(t.getDescription());
        d.setCreatedAt(t.getCreatedAt());
        d.setUpdatedAt(t.getUpdatedAt());
        d.setColumns(t.getColumns().stream().map(TableConfigService::toColumnDto).toList());
        d.setQueryConditions(t.getQueryConditions().stream().map(TableConfigService::toQueryDto).toList());
        d.setButtons(t.getButtons().stream().map(TableConfigService::toButtonDto).toList());
        d.setFormFields(t.getFormFields().stream().map(TableConfigService::toFieldDto).toList());
        d.setSubTables(t.getSubTables().stream().map(TableConfigService::toSubTableDto).toList());
        return d;
    }

    private static ColumnConfigDto toColumnDto(ColumnConfig c) {
        ColumnConfigDto d = new ColumnConfigDto();
        d.setId(c.getId());
        d.setSortOrder(c.getSortOrder());
        d.setDisplayName(c.getDisplayName());
        d.setDataKey(c.getDataKey());
        d.setWidth(c.getWidth());
        d.setAlignment(c.getAlignment());
        d.setVisible(c.getVisible());
        d.setSortable(c.getSortable());
        return d;
    }

    private static QueryConditionDto toQueryDto(QueryCondition q) {
        QueryConditionDto d = new QueryConditionDto();
        d.setId(q.getId());
        d.setSortOrder(q.getSortOrder());
        d.setLabel(q.getLabel());
        d.setFieldKey(q.getFieldKey());
        d.setControlType(q.getControlType());
        d.setMatchMode(q.getMatchMode());
        d.setDefaultValue(q.getDefaultValue());
        d.setValidationRule(q.getValidationRule());
        return d;
    }

    private static TableButtonDto toButtonDto(TableButton b) {
        TableButtonDto d = new TableButtonDto();
        d.setId(b.getId());
        d.setSortOrder(b.getSortOrder());
        d.setName(b.getName());
        d.setCode(b.getCode());
        d.setIcon(b.getIcon());
        d.setActionType(b.getActionType());
        d.setPosition(b.getPosition());
        d.setButtonStyle(b.getButtonStyle());
        d.setEnabled(b.getEnabled());
        d.setEventHandler(b.getEventHandler());
        return d;
    }

    private static FormFieldDto toFieldDto(FormField f) {
        FormFieldDto d = new FormFieldDto();
        d.setId(f.getId());
        d.setSortOrder(f.getSortOrder());
        d.setGroupName(f.getGroupName());
        d.setFieldLabel(f.getFieldLabel());
        d.setFieldCode(f.getFieldCode());
        d.setControlType(f.getControlType());
        d.setLayoutWeight(f.getLayoutWeight());
        d.setRequired(f.getRequired());
        d.setVisible(f.getVisible());
        d.setReadOnly(f.getReadOnly());
        d.setDefaultValue(f.getDefaultValue());
        d.setPlaceholder(f.getPlaceholder());
        d.setOptions(f.getOptions());
        d.setRefEntity(f.getRefEntity());
        return d;
    }

    private static SubTableMappingDto toSubTableDto(SubTableMapping s) {
        SubTableMappingDto d = new SubTableMappingDto();
        d.setId(s.getId());
        d.setSortOrder(s.getSortOrder());
        d.setSubTableName(s.getSubTableName());
        d.setSubTableCode(s.getSubTableCode());
        d.setRelationField(s.getRelationField());
        d.setFilterCondition(s.getFilterCondition());
        d.setShowHeader(s.getShowHeader());
        d.setAllowAddRow(s.getAllowAddRow());
        d.setAllowBatchDelete(s.getAllowBatchDelete());
        d.setHeightMode(s.getHeightMode());
        d.setFixedHeight(s.getFixedHeight());
        d.setColumnsJson(s.getColumnsJson());
        return d;
    }
}
