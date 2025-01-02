package kpi.fict.prist.core.payment.service;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import kpi.fict.prist.core.common.StripeCurrency;
import kpi.fict.prist.core.order.entity.OrderEntity;
import kpi.fict.prist.core.order.service.OrderService;
import kpi.fict.prist.core.payment.dto.PaymentRequest;
import kpi.fict.prist.core.payment.dto.PaymentSessionResponse;
import kpi.fict.prist.core.user.entity.UserProfileEntity;
import kpi.fict.prist.core.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final OrderService orderService;
    private final UserService userService;

    public PaymentService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @SneakyThrows
    @Transactional
    public PaymentSessionResponse createCheckoutSession(String userExternalId, PaymentRequest request) {
        String orderId = request.orderId();

        OrderEntity orderEntity = orderService.getOrderById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found byt id " + orderId));

        String message = "Payment for order %s in restaurant".formatted(orderEntity.getId());
        ProductData productData = ProductData.builder()
            .setName(message)
            .build();

        PriceData priceData = PriceData.builder()
            .setCurrency(StripeCurrency.UAH.getCode())
            .setUnitAmount(request.centsAmount())
            .setProductData(productData)
            .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
            .setPriceData(priceData)
            .setQuantity(1L)
            .build();

        UserProfileEntity userProfileEntity = userService.findByExternalId(userExternalId)
            .orElseThrow(() -> new IllegalArgumentException("User not found byt id " + userExternalId));

        SessionCreateParams params = SessionCreateParams.builder()
            .putMetadata("orderId", orderEntity.getId())
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8000/my-orders")
            .setCancelUrl("http://localhost:8000/my-orders")
            .setCustomerEmail(userProfileEntity.getEmail())
            .addLineItem(lineItem)
            .build();

        Session session = Session.create(params);
        String paymentId = session.getId();

        orderEntity.setPaymentId(paymentId);

        return new PaymentSessionResponse(paymentId);
    }
    
}
