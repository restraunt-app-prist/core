package kpi.fict.prist.core.payment.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import kpi.fict.prist.core.common.StripeCurrency;
import kpi.fict.prist.core.order.entity.OrderEntity;
import kpi.fict.prist.core.order.entity.OrderEntity.OrderStatus;
import kpi.fict.prist.core.order.repository.OrderEntityRepository;
import kpi.fict.prist.core.order.service.OrderService;
import kpi.fict.prist.core.payment.dto.PaymentRequest;
import kpi.fict.prist.core.payment.dto.PaymentSessionResponse;
import kpi.fict.prist.core.user.entity.UserProfileEntity;
import kpi.fict.prist.core.user.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderEntityRepository orderEntityRepository;
    private final String webhookSecretKey;

    public PaymentService(OrderService orderService,
                          UserService userService,
                          OrderEntityRepository orderEntityRepository,
                          @Value("${stripe.webhook.secret.key}") String webhookSecretKey) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderEntityRepository = orderEntityRepository;
        this.webhookSecretKey = webhookSecretKey;
    }

    @SneakyThrows
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
        orderEntityRepository.save(orderEntity);

        return new PaymentSessionResponse(paymentId);
    }

    public String handleWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecretKey);
            String eventType = event.getType();
            switch (eventType) {
                case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
                default -> log.info("Unhandled event type: {}",  eventType);
            }

            return "Webhook handled successfully";
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed: {}", e.getMessage());
            throw new IllegalStateException("Webhook signature verification failed: " + e.getMessage());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
            .getObject()
            .orElse(null);

        if (session == null) {
            log.warn("Checkout session is null!!");
            return;
        }

        String sessionId = session.getId();
        String paymentIntentId = session.getPaymentIntent();
        log.info("Checkout session completed: sessionId={}, paymentIntentId={}", sessionId, paymentIntentId);

        // Find the order by orderId
        String orderId = session.getMetadata().get("orderId");
        OrderEntity order = orderEntityRepository.findById(orderId)
            .orElseThrow(() -> new IllegalStateException("Order not found for payment ID: " + paymentIntentId));

        order.setStatus(OrderStatus.PAID);
        orderEntityRepository.save(order);

        log.info("Order updated to PAID for paymentIntentId={}", paymentIntentId);
    }

}
