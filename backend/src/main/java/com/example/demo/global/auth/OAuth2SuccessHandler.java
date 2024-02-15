package com.example.demo.global.auth;

import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.JwtTokenDTO;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJwtToken(authentication);

        SuccessResponse<JwtTokenDTO> successResponse = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(token))
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