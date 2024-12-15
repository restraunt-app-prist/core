package kpi.fict.prist.core.payment.service;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import kpi.fict.prist.core.payment.entity.PaymentEntity;
import kpi.fict.prist.core.payment.repository.PaymentEntityRepository;

import org.springframework.stereotype.Service;

@Service
public class StripeWebhookService {

    private final PaymentEntityRepository paymentRepository;

    public StripeWebhookService(PaymentEntityRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void processWebhook(String payload, String signatureHeader, String secret) throws Exception {
        Event event = Webhook.constructEvent(payload, signatureHeader, secret);

        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> System.out.println("Unhandled event type: " + event.getType());
        }
    }

    private void handlePaymentSucceeded(Event event) throws Exception {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new IllegalArgumentException("Failed to deserialize PaymentIntent"));

        saveOrUpdatePayment(paymentIntent, "succeeded");
    }

    private void handlePaymentFailed(Event event) throws Exception {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new IllegalArgumentException("Failed to deserialize PaymentIntent"));

        saveOrUpdatePayment(paymentIntent, "failed");
    }

    private void saveOrUpdatePayment(PaymentIntent paymentIntent, String status) {
        PaymentEntity paymentEntity = paymentRepository.findByPaymentId(paymentIntent.getId())
                .map(existingPayment -> {
                    existingPayment.setStatus(status);

                    return existingPayment;
                })
                .orElseGet(() -> {
                    return PaymentEntity.builder()
                        .paymentIntentId(paymentIntent.getId())
                        .status(status)
                        .amount(paymentIntent.getAmount())
                        .currency(paymentIntent.getCurrency())
                        .description(paymentIntent.getDescription())
                        .build();
                });
    
        paymentRepository.save(paymentEntity);
    }
}
