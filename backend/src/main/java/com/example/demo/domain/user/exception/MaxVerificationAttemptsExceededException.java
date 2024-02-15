package com.example.demo.domain.user.exception;

public class MaxVerificationAttemptsExceededException extends RuntimeException {
    public MaxVerificationAttemptsExceededException(String message) {
        super(message);
    }
}
