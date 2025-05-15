package com.cordilleracoffee.paymentadapter.infrastructure.api.client.impl;


import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import com.cordilleracoffee.paymentadapter.application.exception.InvalidPaymentException;
import com.cordilleracoffee.paymentadapter.application.mappers.PaymentMapper;
import com.cordilleracoffee.paymentadapter.infrastructure.api.client.PaymentGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private final WebClient webClient;
    private final PaymentMapper paymentMapper;

    @Override
    public Mono<PaymentGatewayResponse> performPayment(PaymentGatewayRequest gatewayRequest, String idempotencyKey){
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
}
