package com.example.banking.exception;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Counter globalErrorCounter;
    private final Counter insufficientFundsErrCounter;
    private final Counter accountNotFoundErrCounter;
    private final Counter validationErrCounter;
    private final Counter snsPublishErrCounter;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(MeterRegistry meterRegistry) {
        this.globalErrorCounter = meterRegistry.counter("bank.global.errors.count");
        this.insufficientFundsErrCounter = meterRegistry.counter("bank.withdraw.failure.insufficient_funds");
        this.accountNotFoundErrCounter = meterRegistry.counter("bank.withdraw.failure.account_not_found");
        this.validationErrCounter = meterRegistry.counter("bank.withdraw.failure.input_validation");
        this.snsPublishErrCounter = meterRegistry.counter("bank.withdraw.failure.sns_publish");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        globalErrorCounter.increment();
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error occurred");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException ex) {
        log.error("Insufficient funds for accountId {}: {}", ex.getAccountId(), ex.getMessage());
        insufficientFundsErrCounter.increment();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Insufficient funds, accountId: " + ex.getAccountId());
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<String> handleBankAccountNotFound(BankAccountNotFoundException ex) {
        log.error("Account not found - accountId: {}, message: {}", ex.getAccountId(), ex.getMessage());
        accountNotFoundErrCounter.increment();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Account not found, accountId: " + ex.getAccountId());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInputValidationExceptions(MethodArgumentNotValidException ex) {
        validationErrCounter.increment();
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessages.append(error.getDefaultMessage()).append(".");
        });
        log.info("Validation errors: {}", errorMessages);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Validation errors: " + errorMessages);
    }

    @ExceptionHandler(SnsAsyncPublishException.class)
    public ResponseEntity<String> handleSnsExceptions(SnsAsyncPublishException ex) {
        snsPublishErrCounter.increment();
        log.error("Failed to publish message message: {} topic: {}", ex.getMessage(), ex.getTopicArn());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
