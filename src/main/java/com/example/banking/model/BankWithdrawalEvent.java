package com.example.banking.model;

import com.example.banking.enums.WithdrawalStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BankWithdrawalEvent {
    private BigDecimal amount;
    private Long transactionId;
    private Long accountId;
    private WithdrawalStatus status;

    public BankWithdrawalEvent(BigDecimal amount, Long accountId, Long transactionId, WithdrawalStatus status) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.status = status;
    }

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing WithdrawalEvent", e);
        }
    }
}