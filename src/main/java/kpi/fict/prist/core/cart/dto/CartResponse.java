package kpi.fict.prist.core.cart.dto;

import java.time.LocalDateTime;
import java.util.List;
import kpi.fict.prist.core.cart.entity.CartEntity.CartItem;
import lombok.Builder;

@Builder
public record CartResponse(
    String id,
    String userExternalId,
    List<CartItem> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Double totalPrice
) {

}
