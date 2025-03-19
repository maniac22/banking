package com.example.banking.service;

import com.example.banking.exception.SnsAsyncPublishException;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.concurrent.CompletableFuture;

@Service
public class SnsAsyncService {
    private final SnsAsyncClient snsAsyncClient;

    public SnsAsyncService(SnsAsyncClient snsAsyncClient) {
        this.snsAsyncClient = snsAsyncClient;
    }

    public CompletableFuture<String> AsyncPublishMessage(String topicArn, String message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

        return snsAsyncClient.publish(publishRequest)
                .thenApply(PublishResponse::messageId)
                .thenApply(messageId -> messageId)
                .exceptionally(ex -> {
                    throw new SnsAsyncPublishException(message, topicArn);
                });
    }
}
