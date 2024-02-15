package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.user.model.EmailVerification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "이메일 인증코드 발송 요청")
public class SendEmailVerificationCommand {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "중복 체크할 이메일", example = "test@gmail.com")
    private String email;

    public EmailVerification toEmailVerificationDomain() {
        return EmailVerification.builder()
                .email(email)
                .build();
    }
}
