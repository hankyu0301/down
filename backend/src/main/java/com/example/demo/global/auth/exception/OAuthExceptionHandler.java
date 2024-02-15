package com.example.demo.global.auth.exception;

import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class OAuthExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> oAuth2AuthenticationExceptionHandler(OAuth2AuthenticationException e) {

            FailResponse<Map<String, String>> response = FailResponse.<Map<String, String>>builder()
                    .data(Map.of("errorMessage", e.getMessage()))
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
    }

}