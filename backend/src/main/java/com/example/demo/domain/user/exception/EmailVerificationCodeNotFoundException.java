package com.example.demo.domain.user.exception;

public class EmailVerificationCodeNotFoundException extends RuntimeException {
    public EmailVerificationCodeNotFoundException(String message) {
        super(message);
    }
}
