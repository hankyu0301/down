package com.example.demo.domain.user.exception;

public class PendingEmailNotFoundException extends RuntimeException {
    public PendingEmailNotFoundException(String message) {
        super(message);
    }
}
