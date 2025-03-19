package com.example.banking.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SnsAsyncPublishException extends RuntimeException {
    private  String message;
    private  String messageId;
    private  String topicArn;

    public SnsAsyncPublishException(String message, String topicArn) {
        super("Failed to publish SNS message, id: " + message+ ", topicArn: " + topicArn);
        this.topicArn = topicArn;
        this.message = message;
    }
}

