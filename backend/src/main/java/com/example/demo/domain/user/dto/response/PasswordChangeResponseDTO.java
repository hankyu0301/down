package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "비밀번호 변경 응답")
public class PasswordChangeResponseDTO {
        @NotNull
        @Schema(description = "비밀번호 변경 성공 여부", example = "true")
        private boolean success;
}