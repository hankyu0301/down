package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "이메일 존재 체크 응답")
public class EmailExistenceDTO {

        @Email
        @Schema(description = "이메일 중복 체크한 이메일", example = "test@gmail.com")
        private String checkedEmail;

        @NotBlank
        @Schema(description = "이메일 존재 여부", example = "true")
        private boolean existence;
}