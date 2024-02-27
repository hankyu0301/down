package com.example.demo.global.auth;

import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.dto.JwtTokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> tokens = Map.of(
                        "accessToken", jwtTokenProvider.generateAccessToken(authentication),
                        "refreshToken", jwtTokenProvider.generateRefreshToken(authentication)
        );

        SuccessResponse<JwtTokenDTO> successResponse = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(tokens))
                .message("로그인 성공")
                .build();

        String stringResponse = objectMapper.writeValueAsString(successResponse);

        // 응답에 JSON 형태의 토큰을 출력
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(stringResponse);
        response.getWriter().flush();
    }
}