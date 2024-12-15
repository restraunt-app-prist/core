package kpi.fict.prist.core.payment.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private long amount;
    private String description;
}