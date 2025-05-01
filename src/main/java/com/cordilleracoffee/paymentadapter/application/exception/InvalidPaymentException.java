package com.cordilleracoffee.paymentadapter.application.exception;

import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class InvalidPaymentException extends RuntimeException {
    private final PaymentResponse paymentResponse;
    private final HttpStatusCode httpStatus;

    public InvalidPaymentException(String message, PaymentResponse paymentResponse, HttpStatusCode httpStatus) {
        super(message);
        this.paymentResponse = paymentResponse;
        this.httpStatus = httpStatus;
    }
}
