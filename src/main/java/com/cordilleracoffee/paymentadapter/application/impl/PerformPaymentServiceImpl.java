package com.cordilleracoffee.paymentadapter.application.impl;


import com.cordilleracoffee.paymentadapter.application.PerformPaymentService;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import com.cordilleracoffee.paymentadapter.application.mappers.PaymentMapper;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PerformPaymentServiceImpl implements PerformPaymentService {

    private final WebClient webClient;
    private final PaymentMapper paymentMapper;

    @Override
    public Mono<PaymentResponse> performPayment(Mono<PaymentRequest> paymentRequest) {

        return paymentRequest
                .map(paymentMapper::toGatewayRequest)
                .flatMap(this::callPaymentGateway)
                .map(paymentMapper::toResponse);


    }

    private Mono<PaymentGatewayResponse> callPaymentGateway(PaymentGatewayRequest gatewayRequest) {
        return webClient.post()
                .bodyValue(gatewayRequest)
                .retrieve()
                .bodyToMono(PaymentGatewayResponse.class);
    }
}
