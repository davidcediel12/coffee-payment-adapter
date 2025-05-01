package com.cordilleracoffee.paymentadapter.application.dto;

import java.math.BigDecimal;

public record PaymentRequest(PaymentMethod paymentMethod, String cardNumber,
                             String cardHolderName, String expirationDate, String cvv,
                             BigDecimal amount, String currency, String orderId) {
}
