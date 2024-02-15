package com.example.demo.domain.user.model;

import com.example.demo.domain.util.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PendingEmail extends BaseDomain {
    private PendingEmailId id; // 기본키
    private String email; // 이메일
    private String authCode; // 인증 코드
    private Integer authCount; // 인증 시도 횟수

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class PendingEmailId {
        private Long id;
    }
}
