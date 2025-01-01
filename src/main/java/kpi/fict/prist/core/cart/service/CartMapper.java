package kpi.fict.prist.core.cart.service;

import kpi.fict.prist.core.cart.dto.CartResponse;
import kpi.fict.prist.core.cart.entity.CartEntity;
import org.springframework.stereotype.Service;

@Service
public class CartMapper {

    public CartResponse toCartResponse(CartEntity entity, Double totalPrice) {
        return CartResponse.builder()
            .id(entity.getId())
            .userExternalId(entity.getUserExternalId())
            .items(entity.getItems())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .totalPrice(totalPrice)
            .build();
    }

}
