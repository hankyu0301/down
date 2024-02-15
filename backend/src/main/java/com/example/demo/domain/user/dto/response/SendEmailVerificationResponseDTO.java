package com.example.demo.domain.user.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@Schema(description = "이메일 중복 체크 응답")
public class SendEmailVerificationResponseDTO {

        @NotNull
        @Schema(description = "이메일 중복 체크 성공 여부", example = "true")
        private boolean success;

        @Email
        @NotBlank
        @Schema(description = "인증코드를 보낸 이메일", example = "test@gmail.com")
        private String email;
}