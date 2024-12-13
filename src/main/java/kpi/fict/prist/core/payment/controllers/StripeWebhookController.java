package kpi.fict.prist.core.payment.controllers;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kpi.fict.prist.core.payment.service.StripeWebhookService;

import java.io.BufferedReader;

@RestController
@RequestMapping("/api/payments/webhook")
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeWebhookController(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostMapping
    public ResponseEntity<?> handleWebhook(HttpServletRequest request) {
        try (
            BufferedReader reader = request.getReader()
        ) {
            String payload = reader.lines().reduce("", (accumulator, actual) -> accumulator + actual);

            stripeWebhookService.processWebhook(payload, request.getHeader("Stripe-Signature"), webhookSecret);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Webhook processing failed: " + e.getMessage());
        }
    }
}
