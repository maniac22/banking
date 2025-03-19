package com.example.banking.controller;

import com.example.banking.dto.BankAccountWithdrawalRequest;
import com.example.banking.dto.BankAccountWithdrawalResponse;
import com.example.banking.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bank")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccountWithdrawalResponse> withdraw(@RequestBody @Valid BankAccountWithdrawalRequest request) {
        return ResponseEntity.ok(bankAccountService.withdraw(request));
    }
}
