package com.example.demo.domain.user.exception;


import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {

        FailResponse<Map<String, String>> response = FailResponse.<Map<String, String>>builder()
                .data(Map.of("errorMessage", e.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MaxVerificationAttemptsExceededException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> maxVerificationAttemptsExceededExceptionHandler(MaxVerificationAttemptsExceededException e) {

        FailResponse<Map<String, String>> response = FailResponse.<Map<String, String>>builder()
                .data(Map.of("errorMessage", e.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(PendingEmailNotFoundException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> pendingEmailNotFoundExceptionHandler(PendingEmailNotFoundException e) {

        FailResponse<Map<String, String>> response = FailResponse.<Map<String, String>>builder()
                .data(Map.of("errorMessage", e.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}