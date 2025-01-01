package kpi.fict.prist.core.payment.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    record PaymentRequest(Long amount) {
    }

    @Value("${stripe.webhook.secret.key}")
    private String webhookSecretKey;

    @PostMapping("create-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PaymentRequest request) {
        try {
            // Create line items for the session
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("uah")
                        .setUnitAmount(request.amount) // Amount in cents
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("пицца с сыром") // Product name
                                .build()
                        )
                        .build()
                )
                .setQuantity(1L) // Quantity of the product
                .build();

            // Create the session
            SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8000/success")
                .setCancelUrl("http://localhost:8000/cancel")
                .setCustomerEmail("email@email.com")
                .addLineItem(lineItem)
                .build();

            Session session = Session.create(params);

            // Return session ID
            return ResponseEntity.ok(Map.of("sessionId", session.getId()));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecretKey);

            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);
                    log.info("Payment succeeded for ID: " + paymentIntent.getId());
                    // TODO: Fulfill the order, update database, etc.
                    break;

                case "checkout.session.completed":
                    log.info("Checkout session completed.");
                    // TODO: Handle post-checkout actions
                    break;

                default:
                    log.warn("Unhandled event type: " + event.getType());
            }

            return ResponseEntity.ok("Webhook handled successfully");
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
