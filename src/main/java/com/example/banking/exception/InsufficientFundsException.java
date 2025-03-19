package com.example.banking.exception;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InsufficientFundsException extends RuntimeException {
    public Long accountId;
    public BigDecimal amount;
    public BigDecimal balance;
    public InsufficientFundsException(Long accountId,  BigDecimal balance, BigDecimal amount) {
        super("Insufficient funds for bank account, id: " + accountId + ", balance: " + balance+ ", withdrawal_amount: " + amount);
        this.accountId = accountId;
        this.amount = amount;
        this.balance = balance;
    }
}
