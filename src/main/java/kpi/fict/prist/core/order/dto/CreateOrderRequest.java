package kpi.fict.prist.core.order.dto;

import jakarta.validation.constraints.Min;

public record CreateOrderRequest(
    String deliveryLocation,
    @Min(0) Double deliveryDistance,
    String description
) {

}