package kpi.fict.prist.core.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CreateOrderRequest(
    String deliveryLocation,
    @Min(0) @Max(20) Double deliveryDistance,
    String description
) {

}