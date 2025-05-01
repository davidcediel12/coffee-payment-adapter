package com.cordilleracoffee.paymentadapter.application.dto;

import java.math.BigDecimal;

public record PaymentGatewayResponse(String status, String transactionId, String orderId,
                                     BigDecimal amount, String currency, String message) {
}
