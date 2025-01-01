package kpi.fict.prist.core.order.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String paymentMethodId;
    private String location;
}