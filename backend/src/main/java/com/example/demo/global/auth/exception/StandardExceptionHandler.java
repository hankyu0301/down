package com.example.demo.global.auth.exception;

import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StandardExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<FailResponse> illegalAccessExceptionHandler(IllegalAccessException e) {

        FailResponse response = FailResponse.of(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}