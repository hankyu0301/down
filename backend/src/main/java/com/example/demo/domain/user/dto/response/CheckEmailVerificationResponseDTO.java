package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "이메일 인증코드 확인 응답")
public class CheckEmailVerificationResponseDTO {

        @Email
        @NotBlank
        @Schema(description = "이메일", example = "test@gmail.com")
        private String email;

        @NotBlank
        @Schema(description = "이메일 인증코드 확인 결과", example = "true")
        private boolean result;
}