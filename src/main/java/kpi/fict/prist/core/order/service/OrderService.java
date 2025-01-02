package kpi.fict.prist.core.order.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kpi.fict.prist.core.cart.entity.CartEntity.CartItem;
import kpi.fict.prist.core.order.dto.CreateOrderRequest;
import kpi.fict.prist.core.order.dto.TotalSumRequest;
import kpi.fict.prist.core.order.dto.TotalSumResponse;
import kpi.fict.prist.core.order.entity.OrderEntity.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kpi.fict.prist.core.cart.entity.CartEntity;
import kpi.fict.prist.core.cart.service.CartService;
import kpi.fict.prist.core.menu.entity.MenuItemEntity;
import kpi.fict.prist.core.menu.service.MenuService;
import kpi.fict.prist.core.order.entity.OrderEntity;
import kpi.fict.prist.core.order.entity.OrderEntity.OrderStatus;
import kpi.fict.prist.core.order.repository.OrderEntityRepository;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Double DELIVERY_PRICE_PER_KILOMETER_UAH = 20.0;

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
    public OrderEntity createOrder(String userExternalId, CreateOrderRequest orderRequest) {

        // Fetch the user's cart
        CartEntity cart = cartService.getCartByUserExternalId(userExternalId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order with an empty cart.");
        }

        // Convert CartEntity.CartItem to OrderEntity.CartItem and fetch additional details from MenuService
        List<MenuItemEntity> cartMenuItems = cart.getItems().stream()
            .map(cartItem -> menuService.getMenuItemById(cartItem.getMenuItemId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
        List<OrderItem> orderItems = cartMenuItems.stream()
            .map(menuItem -> {
                CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getMenuItemId().equals(menuItem.getId()))
                    .findFirst()
                    .orElse(null);

                if (cartItem == null) {
                    return null;
                }
                double itemPrice = menuItem.getPrice() * cartItem.getQuantity();
                totalPrice.updateAndGet(price -> price + itemPrice);

                return OrderItem.builder()
                    .menuItemId(menuItem.getId())
                    .quantity(cartItem.getQuantity())
                    .build();
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        if (orderRequest.deliveryDistance() != null) {
            totalPrice.updateAndGet(price -> price + orderRequest.deliveryDistance() * DELIVERY_PRICE_PER_KILOMETER_UAH);
        }
        // Create and save the order
        OrderEntity order = OrderEntity.builder()
            .items(orderItems)
            .totalPrice(totalPrice.get())
            .status(OrderStatus.PENDING)
            .userExternalId(userExternalId)
            .location(orderRequest.deliveryLocation())
            .notes(orderRequest.description())
            .build();

        order = orderRepository.save(order);

        cartService.clearCart(userExternalId);

        return order;
    }

    public TotalSumResponse calculateTotalSum(String userExternalId, TotalSumRequest request) {
        CartEntity cart = cartService.getCartByUserExternalId(userExternalId);
        if (CollectionUtils.isEmpty(cart.getItems())) {
            throw new IllegalStateException("Cannot create an order with an empty cart.");
        }
        double totalSum = 0.0;
        for (CartItem item : cart.getItems()) {
            Optional<MenuItemEntity> menuItemById = menuService.getMenuItemById(item.getMenuItemId());
            if (menuItemById.isPresent()) {
                totalSum += menuItemById.get().getPrice() * item.getQuantity();
            }
        }
        totalSum += request.deliveryDistance() * DELIVERY_PRICE_PER_KILOMETER_UAH;
        return new TotalSumResponse(totalSum);
    }

    public Optional<OrderEntity> findByPaymentId(String paymentId) {
        return orderRepository.findByPaymentId(paymentId);
    }

}
