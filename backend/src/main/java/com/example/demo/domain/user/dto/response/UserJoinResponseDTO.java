package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원가입 응답 DTO")
public class UserJoinResponseDTO {
        @NotNull
        @Schema(description = "사용자 식별자", example = "1")
        private Long id;

        @Email
        @Schema(description = "로그인 이메일", example = "test@gmail.com")
        private String email;
}