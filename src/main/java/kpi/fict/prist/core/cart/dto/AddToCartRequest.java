package kpi.fict.prist.core.cart.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String menuItemId;
    private int quantity;
}
