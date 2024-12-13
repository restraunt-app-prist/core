package kpi.fict.prist.core.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    private long amount;
    private String description;
}