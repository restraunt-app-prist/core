package kpi.fict.prist.core.menu.dto;

import lombok.Data;

import kpi.fict.prist.core.common.MenuCategory;

import java.math.BigDecimal;

@Data
public class CreateMenuItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private Boolean available;
}
