package kpi.fict.prist.core.cart.service;

import java.util.Collections;
import java.util.List;
import kpi.fict.prist.core.cart.entity.CartEntity.CartItem;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;
import kpi.fict.prist.core.menu.service.MenuService;
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
    private final MenuService menuService;

    public CartService(CartEntityRepository cartRepository, MenuItemEntityRepository menuItemRepository, MenuService menuService) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuService = menuService;
    }

    public CartEntity getCartByUserExternalId(String userExternalId) {
        return cartRepository.findByUserExternalId(userExternalId)
            .orElseGet(() -> {
                CartEntity newCart = new CartEntity(userExternalId);
                return cartRepository.save(newCart);
            });
    }

    public CartEntity addItemToCart(String userExternalId, AddToCartRequest request) {
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

        return cartRepository.save(cart);
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

    public void removeItemFromCart(String userExternalId, String menuItemId) {
        CartEntity cart = getCartByUserExternalId(userExternalId);
        List<CartItem> items = cart.getItems();
        List<CartItem> newItems = items.stream()
            .filter(item -> !item.getMenuItemId().equals(menuItemId))
            .toList();
        cart.setItems(newItems);
        cartRepository.save(cart);
    }

    public Double totalPrice(String userExternalId) {
        CartEntity cart = getCartByUserExternalId(userExternalId);
        List<CartItem> items = cart.getItems();
        return Optional.ofNullable(items).orElse(Collections.emptyList()).stream()
            .map(item -> menuService.getMenuItemById(item.getMenuItemId())
                .map(menuItem -> menuItem.getPrice() * item.getQuantity())
                .orElse(0.0))
            .reduce(Double::sum)
            .orElse(0.0);
    }

}
