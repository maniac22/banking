package com.example.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BankAccountWithdrawalRequest(
        @NotNull(message = "account ID is required") @Positive(message = "account ID must be positive") Long accountId,
        @NotNull(message = "withdrawal amount is required") @Positive(message = "withdrawal amount must be greater than zero") BigDecimal amount) {
}
