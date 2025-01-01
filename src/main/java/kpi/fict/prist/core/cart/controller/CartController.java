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
    public CartEntity getCart(@AuthenticationPrincipal Jwt jwt) {
        String userExternalId = jwt.getSubject();
        return cartService.getCartByUserExternalId(userExternalId);
    }

    @PostMapping("items")
    public CartEntity addItemToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody AddToCartRequest request) {
        String userExternalId = jwt.getSubject();
        return cartService.addItemToCart(userExternalId, request);
    }

    @DeleteMapping("items/{menuItemId}")
    public void removeItemFromCart(@AuthenticationPrincipal Jwt jwt, @PathVariable String menuItemId) {
        String userExternalId = jwt.getSubject();
        cartService.removeItemFromCart(userExternalId, menuItemId);
    }

    @DeleteMapping
    public void clearCart(@AuthenticationPrincipal Jwt jwt) {
        String userExternalId = jwt.getSubject();
        cartService.clearCart(userExternalId);
    }
}
