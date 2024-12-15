package kpi.fict.prist.core.menu.dto;

import lombok.Builder;
import lombok.Data;

import kpi.fict.prist.core.common.MenuCategory;

import java.math.BigDecimal;

@Data
@Builder
public class CreateMenuItemRequest {
    private String name;
    private String description;
    private String pictureUrl;
    private BigDecimal price;
    private MenuCategory category;
    private Boolean available;
}
