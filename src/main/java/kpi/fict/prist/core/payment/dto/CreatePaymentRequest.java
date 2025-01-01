package kpi.fict.prist.core.payment.dto;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
    @NotNull Long amount,
    @NotNull String description
) {
}