package com.cordilleracoffee.paymentadapter.infrastructure.api.controller;


import com.cordilleracoffee.paymentadapter.application.PerformPaymentService;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentAdapterController {

    private final PerformPaymentService performPaymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PaymentResponse> performPayment(@RequestBody @Valid Mono<PaymentRequest> paymentRequest){

        return performPaymentService.performPayment(paymentRequest);

    }

}
