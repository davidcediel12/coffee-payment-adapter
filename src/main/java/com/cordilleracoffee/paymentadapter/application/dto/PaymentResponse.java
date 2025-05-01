package com.cordilleracoffee.paymentadapter.application.dto;

import java.math.BigDecimal;

public record PaymentResponse(String status, String transactionId, String orderId,
                              BigDecimal amount, String currency, String message) {
}
