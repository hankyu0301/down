package com.example.demo.global.auth.jwt.service;

import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.entity.AccessToken;
import com.example.demo.global.auth.jwt.entity.RefreshToken;
import com.example.demo.global.auth.jwt.repository.AccessTokenRepository;
import com.example.demo.global.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class JwtService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 성공 시 인증 토큰과 재발급 토큰을 저장
    public void save(String accessToken, String refreshToken) {
        Assert.notNull(accessToken, "accessToken must not be null");
        Assert.notNull(refreshToken, "refreshToken must not be null");

        String email = jwtTokenProvider.getEmail(accessToken);
        Long expire = jwtTokenProvider.getExpire(accessToken);

        AccessToken accessTokenEntity = AccessToken.builder()
                .email(email)
                .accessToken(accessToken)
                .expiredAt(expire)
                .isUsable(true)
                .build();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .expiredAt(expire)
                .isUsable(true)
                .build();

        accessTokenRepository.save(accessTokenEntity);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void save(Map<String, String> tokens) {
        save(tokens.get("accessToken"), tokens.get("refreshToken"));
    }
}