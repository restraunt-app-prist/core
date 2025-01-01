package kpi.fict.prist.core.order.controller;

import kpi.fict.prist.core.order.dto.CreateOrderRequest;
import kpi.fict.prist.core.order.entity.OrderEntity;
import kpi.fict.prist.core.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("my")
    public ResponseEntity<List<OrderEntity>> getOrdersByUserExternalId(@AuthenticationPrincipal Jwt jwt) {
        String userExternalId = jwt.getSubject();
        List<OrderEntity> orders = orderService.getOrdersByUserExternalId(userExternalId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable String id) {
        Optional<OrderEntity> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderEntity createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateOrderRequest request) {
        String userExternalId = jwt.getSubject();
        return orderService.createOrder(userExternalId, request);
    }
}
