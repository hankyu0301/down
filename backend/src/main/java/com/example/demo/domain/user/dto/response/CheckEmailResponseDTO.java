package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@Schema(description = "이메일 중복 체크 응답")
public class CheckEmailResponseDTO {

    @Email
    @NotNull
    @Schema(description = "중복 체크한 이메일", example = "test@gmail.com")
    private String checkedEmail;

    @NotNull
    @Schema(description = "이메일 사용 가능 여부", example = "true")
    private boolean available;
}
