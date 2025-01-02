package kpi.fict.prist.core.payment.controller;

import kpi.fict.prist.core.payment.dto.PaymentRequest;
import kpi.fict.prist.core.payment.dto.PaymentSessionResponse;
import kpi.fict.prist.core.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("create-session")
    public PaymentSessionResponse createCheckoutSession(@AuthenticationPrincipal Jwt jwt, @RequestBody PaymentRequest request) {
        return paymentService.createCheckoutSession(jwt.getSubject(), request);
    }

    @PostMapping("webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        return ResponseEntity.ok(paymentService.handleWebhook(payload, sigHeader));
    }
}
