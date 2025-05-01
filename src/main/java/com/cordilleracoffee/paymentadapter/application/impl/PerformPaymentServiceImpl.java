package com.cordilleracoffee.paymentadapter.application.impl;


import com.cordilleracoffee.paymentadapter.application.PerformPaymentService;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import com.cordilleracoffee.paymentadapter.application.exception.InvalidPaymentException;
import com.cordilleracoffee.paymentadapter.application.exception.PaymentGatewayFailureException;
import com.cordilleracoffee.paymentadapter.application.mappers.PaymentMapper;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformPaymentServiceImpl implements PerformPaymentService {

    private final WebClient webClient;
    private final PaymentMapper paymentMapper;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    @Override
    public Mono<PaymentResponse> performPayment(Mono<PaymentRequest> paymentRequest, String idempotencyKey) {

        return paymentRequest
                .map(paymentMapper::toGatewayRequest)
                .flatMap(request -> {
                            Mono<PaymentGatewayResponse> paymentCall = callPaymentGateway(request, idempotencyKey)
                                    .retryWhen(Retry.backoff(3, Duration.ofMillis(500))
                                            .filter(PaymentGatewayFailureException.class::isInstance));
                            return reactiveCircuitBreaker.run(paymentCall, this::openCircuit);
                        }
                )
                .map(paymentMapper::toResponse);


    }

    private Mono<PaymentGatewayResponse> callPaymentGateway(PaymentGatewayRequest gatewayRequest, String idempotencyKey) {

        return Mono.defer(() -> webClient.post()
                .header("Idempotency-Key", idempotencyKey)
                .bodyValue(gatewayRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(PaymentGatewayResponse.class)
                                .flatMap(errorResponse ->
                                        Mono.error(new InvalidPaymentException("Client error while calling payment gateway",
                                                paymentMapper.toResponse(errorResponse), response.statusCode()))))
                .bodyToMono(PaymentGatewayResponse.class)
                .doOnError(e ->
                        log.error("Error calling payment gateway: ", e)));
    }


    public Mono<PaymentGatewayResponse> openCircuit(Throwable throwable) {

        if (throwable instanceof InvalidPaymentException) {
            return Mono.error(throwable);
        }

        if (throwable instanceof WebClientResponseException e) {
            return Mono.error(new PaymentGatewayFailureException("Error performing payment",
                    paymentMapper.toResponse(e.getResponseBodyAs(PaymentGatewayResponse.class)), e));
        }

        if (throwable instanceof CallNotPermittedException) {
            return Mono.error(new PaymentGatewayFailureException("Circuit breaker is open, call is not permitted",
                    new PaymentResponse("FAILURE", null, null, null, null, throwable.getMessage()),
                    null));
        }

        return Mono.error(new PaymentGatewayFailureException("Error performing payment",
                new PaymentResponse("FAILURE", null, null, null, null, throwable.getMessage()),
                null));

    }
}
