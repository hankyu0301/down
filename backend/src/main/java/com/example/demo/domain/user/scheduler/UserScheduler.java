package com.example.demo.domain.user.scheduler;


import com.example.demo.domain.user.repository.PendingEmailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserScheduler {

        private final PendingEmailsRepository pendingEmailsRepository;

        @Scheduled(cron = "0 0 * * * *") // 매 시간 0분 0초마다 실행
        public void deleteUnverifiedUsers() {
            // 현재 시간에서 10분을 뺀 시간을 구한다.
            LocalDateTime currentTimeMinus10 = LocalDateTime.now().minusMinutes(10);
            long deleteCount = pendingEmailsRepository.deleteByCreatedAtBefore(currentTimeMinus10);
            log.info("삭제된 PendingEmail 레코드 수: {}", deleteCount);
        }
}