package kpi.fict.prist.core.order.dto;

import jakarta.validation.constraints.Min;

public record TotalSumRequest(
    @Min(0) Double deliveryDistance
) {

}
