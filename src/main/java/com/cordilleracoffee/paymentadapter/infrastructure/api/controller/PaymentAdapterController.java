package com.cordilleracoffee.paymentadapter.infrastructure.api.controller;


import com.cordilleracoffee.paymentadapter.application.PerformPaymentService;
import com.cordilleracoffee.paymentadapter.application.exception.InvalidPaymentException;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentAdapterController {

    private final PerformPaymentService performPaymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<PaymentResponse>> performPayment(@RequestBody @Valid Mono<PaymentRequest> paymentRequest,
                                                                @RequestHeader("Idempotency-Key") String idempotencyKey) {

        return performPaymentService.performPayment(paymentRequest, idempotencyKey)
                .map(ResponseEntity::ok)
                .onErrorResume(InvalidPaymentException.class, e ->
                        Mono.just(new ResponseEntity<>(e.getPaymentResponse(),
                                Optional.ofNullable(e.getHttpStatus())
                                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))));

    }

}
