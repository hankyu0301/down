package com.example.demo.global.auth.jwt;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class JwtTokenDTO {
    private String accessToken;
    private String tokenType;

    public static JwtTokenDTO of(String accessToken) {
        return new JwtTokenDTO(accessToken, "Bearer");
    }
}