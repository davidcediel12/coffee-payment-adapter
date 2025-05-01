package com.cordilleracoffee.paymentadapter.application.impl;


import com.cordilleracoffee.paymentadapter.application.PerformPaymentService;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import com.cordilleracoffee.paymentadapter.application.exception.PaymentFailureException;
import com.cordilleracoffee.paymentadapter.application.mappers.PaymentMapper;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformPaymentServiceImpl implements PerformPaymentService {

    private final WebClient webClient;
    private final PaymentMapper paymentMapper;

    @Override
    public Mono<PaymentResponse> performPayment(Mono<PaymentRequest> paymentRequest, String idempotencyKey) {

        return paymentRequest
                .map(paymentMapper::toGatewayRequest)
                .flatMap(gatewayRequest -> callPaymentGateway(gatewayRequest, idempotencyKey))
                .onErrorMap(WebClientResponseException.class, e ->
                        new PaymentFailureException("Error performing payment",
                                paymentMapper.toResponse(e.getResponseBodyAs(PaymentGatewayResponse.class)), e))
                .map(paymentMapper::toResponse);


    }

    private Mono<PaymentGatewayResponse> callPaymentGateway(PaymentGatewayRequest gatewayRequest, String idempotencyKey) {
        return webClient.post()
                .header("Idempotency-Key", idempotencyKey)
                .bodyValue(gatewayRequest)
                .retrieve()
                .bodyToMono(PaymentGatewayResponse.class)
                .doOnError(e ->
                        log.error("Error calling payment gateway: {}", e.getMessage()));
    }
}
