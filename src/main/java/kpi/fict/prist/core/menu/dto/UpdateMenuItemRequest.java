package kpi.fict.prist.core.menu.dto;

import lombok.Data;

import java.math.BigDecimal;

import kpi.fict.prist.core.common.MenuCategory;

@Data
public class UpdateMenuItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private Boolean available;
}
