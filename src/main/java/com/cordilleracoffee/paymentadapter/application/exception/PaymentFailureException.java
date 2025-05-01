package com.cordilleracoffee.paymentadapter.application.exception;

import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class PaymentFailureException extends RuntimeException {

    private final PaymentResponse paymentResponse;
    private final WebClientResponseException webClientResponseException;

    public PaymentFailureException(String message, PaymentResponse paymentResponse,
                                   WebClientResponseException webClientResponseException) {
        super(message, webClientResponseException);
        this.paymentResponse = paymentResponse;
        this.webClientResponseException = webClientResponseException;
    }
}
