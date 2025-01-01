package kpi.fict.prist.core.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private String userExternalId;
    private String paymentMethodId;
    private String location;
    private String notes;
}

