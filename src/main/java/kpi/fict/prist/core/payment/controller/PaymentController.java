package kpi.fict.prist.core.payment.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import kpi.fict.prist.core.payment.dto.PaymentRequest;
import kpi.fict.prist.core.payment.dto.PaymentSessionResponse;
import kpi.fict.prist.core.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret.key}")
    private String webhookSecretKey;

    @PostMapping("create-session")
    public PaymentSessionResponse createCheckoutSession(@AuthenticationPrincipal Jwt jwt, @RequestBody PaymentRequest request) {
        return paymentService.createCheckoutSession(jwt.getSubject(), request);
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
