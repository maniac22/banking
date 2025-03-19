package com.example.banking.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountNotFoundException extends RuntimeException {
    public Long accountId;
    public BankAccountNotFoundException(Long accountId) {
        super(String.format("bank account not found, id: %d",accountId));
    }
}
