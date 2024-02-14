package com.example.demo.global.exception;

import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> illegalAccessExceptionHandler(IllegalAccessException e) {

        FailResponse<Map<String, String>> response = FailResponse.<Map<String, String>>builder()
                .data(Map.of("errorMessage", e.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}