package kpi.fict.prist.core.order.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kpi.fict.prist.core.cart.entity.CartEntity;
import kpi.fict.prist.core.cart.service.CartService;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;
import kpi.fict.prist.core.menu.service.MenuService;
import kpi.fict.prist.core.order.dto.OrderRequest;
import kpi.fict.prist.core.order.entity.OrderEntity;
import kpi.fict.prist.core.order.entity.OrderEntity.OrderStatus;
import kpi.fict.prist.core.order.repository.OrderEntityRepository;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderEntityRepository orderRepository;
    private final CartService cartService;
    private final MenuService menuService;

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderEntity> getOrdersByUserExternalId(String userExternalId) {
        return orderRepository.findByUserExternalId(userExternalId);
    }

    public Optional<OrderEntity> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public OrderEntity createOrder(OrderRequest orderRequest) {
        String userExternalId = orderRequest.getUserExternalId();

        // Fetch the user's cart
        CartEntity cart = cartService.getCartByUserExternalId(userExternalId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order with an empty cart.");
        }

        // Convert CartEntity.CartItem to OrderEntity.CartItem and fetch additional details from MenuService
        List<MenuItemEntity> menuItems = cart.getItems().stream()
        .map(cartItem -> {
            var menuItem = menuService.getMenuItemById(cartItem.getMenuItemId());
            return menuItem.orElse(null);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
        List<OrderEntity.CartItem> orderItems = menuItems.stream()
        .map(menuItem -> {
            var cartItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);
    
            if (cartItem != null) {
                double itemPrice = menuItem.getPrice() * cartItem.getQuantity();
                totalPrice.updateAndGet(price -> price + itemPrice);

                return OrderEntity.CartItem.builder()
                    .menuItemId(menuItem.getId())
                    .quantity(cartItem.getQuantity())
                    .build();
            } else {
                return null;
            }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

        // Create and save the order
        OrderEntity order = OrderEntity.builder()
            .items(orderItems)
            .totalPrice(totalPrice.get())
            .status(OrderStatus.PENDING)
            .userExternalId(userExternalId)
            .location(orderRequest.getLocation())
            .notes(orderRequest.getNotes())
            .build();

        order = orderRepository.save(order);

        // Clear the cart after order creation
        cartService.clearCart(userExternalId);

        return order;
    }
}
