package kpi.fict.prist.core.payment.service;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import kpi.fict.prist.core.common.StripeCurrency;
import kpi.fict.prist.core.payment.dto.CreatePaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final StripeCurrency DEFAULT_CURRENCY = StripeCurrency.UAH;

    public PaymentIntent createPaymentIntent(CreatePaymentRequest request) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(request.getAmount())
            .setCurrency(DEFAULT_CURRENCY.getCode())
            .setDescription(request.getDescription())
            .build();

        return PaymentIntent.create(params);
    }
}
