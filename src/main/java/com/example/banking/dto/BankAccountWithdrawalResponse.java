package com.example.banking.dto;

import java.math.BigDecimal;

public record BankAccountWithdrawalResponse(Long transactionId, BigDecimal balance) {
}
