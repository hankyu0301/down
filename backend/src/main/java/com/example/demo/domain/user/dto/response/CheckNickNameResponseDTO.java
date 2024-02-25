package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckNickNameResponseDTO {

    @NotNull
    @Schema(description = "중복 체크한 닉네임", example = "페이커")
    String nickName;

    @NotNull
    @Schema(description = "닉네임 사용 가능 여부", example = "true")
    private boolean available;
}
