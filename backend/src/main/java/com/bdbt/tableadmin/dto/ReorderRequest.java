package com.bdbt.tableadmin.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 排序请求：批量更新 sortOrder。
 */
public class ReorderRequest {

    @NotNull(message = "排序列表不能为空")
    private List<Item> items;

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public static class Item {
        private Long id;
        private Integer sortOrder;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }
}
