package com.example.banking.service;

import com.example.banking.model.BankWithdrawalEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BankWithdrawalEventService {
    private final SnsAsyncService snsService;

    @Value("${aws.sns.topic.withdrawal-events}")
    private String withdrawalTopicArn;

    public BankWithdrawalEventService(SnsAsyncService snsService, @Value("${aws.sns.topic.withdrawal-events}") String withdrawalTopicArn){
        this.snsService = snsService;
        this.withdrawalTopicArn = withdrawalTopicArn;
    }

    @Async
    public CompletableFuture<String> sendWithdrawalEvent(BankWithdrawalEvent event) {
        return snsService.AsyncPublishMessage(this.withdrawalTopicArn, event.toJson());
    }
}
