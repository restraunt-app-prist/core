package kpi.fict.prist.core.cart.controller;

import kpi.fict.prist.core.cart.dto.AddToCartRequest;
import kpi.fict.prist.core.cart.entity.CartEntity;
import kpi.fict.prist.core.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartEntity> getCart(@AuthenticationPrincipal Jwt jwt) {
        String userExternalId = jwt.getSubject();

        CartEntity cart = cartService.getCartByUserExternalId(userExternalId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("items")
    public ResponseEntity<String> addItemToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody AddToCartRequest request) {
        String userExternalId = jwt.getSubject();
        cartService.addItemToCart(userExternalId, request);
        return ResponseEntity.ok("Item added to cart successfully.");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal Jwt jwt) {
        String userExternalId = jwt.getSubject();

        cartService.clearCart(userExternalId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }
}
