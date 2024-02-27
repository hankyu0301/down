package com.example.demo.global.auth.jwt.scheduler;

import com.example.demo.global.auth.jwt.repository.AccessTokenRepository;
import com.example.demo.global.auth.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtScheduler {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 * * * *") // 1시간 마다 실행
    public void deleteExpiredTokens() {
        Date now = new Date();
        long deleteCount = accessTokenRepository.deleteByExpiredAtBefore(now.getTime());
        log.info("삭제된 AccessToken 레코드 수: {}", deleteCount);

        deleteCount = refreshTokenRepository.deleteByExpiredAtBefore(now.getTime());
        log.info("삭제된 RefreshToken 레코드 수: {}", deleteCount);
    }
}