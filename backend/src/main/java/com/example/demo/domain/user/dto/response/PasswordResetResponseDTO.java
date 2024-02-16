package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "비밀번호 재설정 응답")
public class PasswordResetResponseDTO {

    @Schema(description = "임시 비밀번호를 전송한 이메일 아이디", example = "test@gmail.com")
    private String email;

    @Schema(description = "임시 비밀번호 전송 결과", example = "true")
    private boolean result;
}