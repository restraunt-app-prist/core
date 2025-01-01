package kpi.fict.prist.core.cart.service;

import org.springframework.stereotype.Service;

import kpi.fict.prist.core.cart.dto.AddToCartRequest;
import kpi.fict.prist.core.cart.entity.CartEntity;
import kpi.fict.prist.core.cart.repository.CartEntityRepository;
import kpi.fict.prist.core.menu.repository.MenuItemEntityRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    private final CartEntityRepository cartRepository;
    private final MenuItemEntityRepository menuItemRepository;

    public CartService(CartEntityRepository cartRepository, MenuItemEntityRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public CartEntity getCartByUserExternalId(String userExternalId) {
        return cartRepository.findByUserExternalId(userExternalId)
            .orElseGet(() -> {
                CartEntity newCart = new CartEntity(userExternalId);
                return cartRepository.save(newCart);
            });
    }

    public void addItemToCart(String userExternalId, AddToCartRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (!menuItemRepository.existsById(request.getMenuItemId())) {
            throw new IllegalArgumentException("Menu item with ID " + request.getMenuItemId() + " does not exist.");
        }

        CartEntity cart = cartRepository.findByUserExternalId(userExternalId)
            .orElseGet(() -> createNewCart(userExternalId));

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        Optional<CartEntity.CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getMenuItemId().equals(request.getMenuItemId()))
            .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            cart.getItems().add(new CartEntity.CartItem(request.getMenuItemId(), request.getQuantity()));
        }

        cartRepository.save(cart);
    }

    private CartEntity createNewCart(String userExternalId) {
        return CartEntity.builder()
            .userExternalId(userExternalId)
            .build();
    }

    public void clearCart(String userExternalId) {
        CartEntity cart = getCartByUserExternalId(userExternalId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
