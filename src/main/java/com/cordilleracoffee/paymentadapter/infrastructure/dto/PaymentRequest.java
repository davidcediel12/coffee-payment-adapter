package com.cordilleracoffee.paymentadapter.infrastructure.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(@NotBlank(message = "Payment method is required")
                             @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|PAYPAL",
                                     message = "Payment method must be CREDIT_CARD, DEBIT_CARD or PAYPAL")
                             String paymentMethod,

                             @NotBlank(message = "Card number is required")
                             @Size(min = 15, max = 16, message = "Card number must be 15 or 16 digits")
                             @Pattern(regexp = "\\d+", message = "Card number must contain only digits")
                             String cardNumber,

                             @NotBlank(message = "Expiration date is required")
                             @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}",
                                     message = "Expiration date must be in MM/YY format")
                             String expirationDate,

                             @NotBlank(message = "Card holder name is required")
                             @Size(min = 2, max = 100, message = "Card holder name must be between 2 and 100 characters")
                             String cardHolder,

                             @NotBlank(message = "CVV is required")
                             @Size(min = 3, max = 4, message = "CVV must be 3 or 4 digits")
                             @Pattern(regexp = "\\d+", message = "CVV must contain only digits")
                             String cvv,

                             @NotNull(message = "Amount is required")
                             @Positive(message = "Amount must be positive")
                             @DecimalMax(value = "1000000", message = "Amount must be less than 1,000,000")
                             BigDecimal amount,

                             @NotBlank(message = "Currency is required")
                             @Size(min = 3, max = 3, message = "Currency must be 3 characters")
                             @Pattern(regexp = "[A-Z]{3}", message = "Currency must be in ISO format (e.g. USD)")
                             String currency,

                             @NotNull(message = "Cart ID is required")
                             UUID cartId
) {
}