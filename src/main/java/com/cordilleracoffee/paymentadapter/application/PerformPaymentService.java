package com.cordilleracoffee.paymentadapter.application;

import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import reactor.core.publisher.Mono;

public interface PerformPaymentService {
    Mono<PaymentResponse> performPayment(Mono<PaymentRequest> paymentRequest, String idempotencyKey);
}
