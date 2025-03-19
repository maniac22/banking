package com.example.banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    @Column(nullable = false)
    private BigDecimal balanceBefore;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public BankTransaction(Long accountId, BigDecimal balanceAfter, BigDecimal balanceBefore) {
        this.accountId = accountId;
        this.balanceAfter = balanceAfter;
        this.balanceBefore = balanceBefore;
        this.createdAt =  LocalDateTime.now();
    }
}
