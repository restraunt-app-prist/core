package kpi.fict.prist.core.order.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String userExternalId;
    private String paymentMethodId;
}

