package com.example.demo.domain.user.model;

import com.example.demo.domain.util.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EmailVerification extends BaseDomain {
    private String email; // 이메일
    private String code; // 발송된 인증코드}
}
