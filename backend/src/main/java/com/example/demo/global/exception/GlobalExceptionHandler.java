package com.example.demo.global.exception;

import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailResponse> illegalAccessExceptionHandler(IllegalArgumentException e) {
        return responseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailResponse> customExceptionHandler(IllegalArgumentException e) {
        return responseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<FailResponse> oAuth2AuthenticationExceptionHandler(OAuth2AuthenticationException e) {
        return responseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<FailResponse> responseEntity(HttpStatus status, String message) {
        FailResponse response = FailResponse.of(message);
        return ResponseEntity
                .status(status)
                .body(response);
    }
}