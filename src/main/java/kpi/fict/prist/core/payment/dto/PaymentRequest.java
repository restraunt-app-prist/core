package kpi.fict.prist.core.payment.dto;

import jakarta.validation.constraints.Min;

public record PaymentRequest(String orderId, @Min(0) Long centsAmount) {

}
