package com.example.banking.service;

import com.example.banking.dto.BankAccountWithdrawalRequest;
import com.example.banking.dto.BankAccountWithdrawalResponse;
import com.example.banking.enums.WithdrawalStatus;
import com.example.banking.exception.BankAccountNotFoundException;
import com.example.banking.exception.InsufficientFundsException;
import com.example.banking.model.BankAccount;
import com.example.banking.model.BankTransaction;
import com.example.banking.model.BankWithdrawalEvent;
import com.example.banking.repository.BankAccountRepository;
import com.example.banking.repository.BankTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private BankTransactionRepository bankTransactionRepository;
    private BankWithdrawalEventService bankWithdrawalEventService;

    @Transactional
    public BankAccountWithdrawalResponse withdraw(BankAccountWithdrawalRequest request) {
        // Get the associated account
        final Long accountId = request.accountId();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(accountId));
        final BigDecimal balanceBefore = account.getBalance();

        // Withdraw funds
        final int affectedRows = bankAccountRepository.withdrawIfSufficientBalance(account.getId(), request.amount());
        if (affectedRows == 0) {
           throw new InsufficientFundsException(accountId, account.getBalance(), request.amount());
        }

        // Record transaction
        final BankTransaction transaction = new BankTransaction(request.accountId(), account.getBalance(),
                balanceBefore);
        bankTransactionRepository.save(transaction);

        // Notify asynchronously
        final BankWithdrawalEvent event = new BankWithdrawalEvent(request.amount(), request.accountId(),
                transaction.getId(), WithdrawalStatus.SUCCESS);
        bankWithdrawalEventService.sendWithdrawalEvent(event);

        return new BankAccountWithdrawalResponse(transaction.getId(), account.getBalance());
    }
}
