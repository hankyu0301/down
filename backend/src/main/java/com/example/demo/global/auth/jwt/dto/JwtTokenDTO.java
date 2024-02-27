package com.example.demo.global.auth.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class JwtTokenDTO {

    @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8")
    private String accessToken;

    @Schema(description = "재발급 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8")
    private String refreshToken;

    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;

    public static JwtTokenDTO of(Map<String, String> tokens) {
        return new JwtTokenDTO(tokens.get("accessToken"), tokens.get("refreshToken"), "Bearer");
    }
}