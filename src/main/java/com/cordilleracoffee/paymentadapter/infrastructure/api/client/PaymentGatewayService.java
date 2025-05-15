package com.cordilleracoffee.paymentadapter.infrastructure.api.client;

import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import reactor.core.publisher.Mono;

public interface PaymentGatewayService {
    Mono<PaymentGatewayResponse> performPayment(PaymentGatewayRequest gatewayRequest, String idempotencyKey);
}
