package com.cordilleracoffee.paymentadapter.infrastructure.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(String status, UUID transactionId, UUID cartId, BigDecimal amount,
                              String currency, String message){
}
