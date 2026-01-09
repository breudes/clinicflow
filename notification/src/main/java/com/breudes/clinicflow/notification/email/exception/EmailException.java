package com.breudes.clinicflow.notification.email.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message, Exception ex) {
        super(message);
    }
}