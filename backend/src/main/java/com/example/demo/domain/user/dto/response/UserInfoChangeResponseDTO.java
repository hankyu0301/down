package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 정보 변경 응답")
public class UserInfoChangeResponseDTO {
    @Schema(description = "회원 정보 변경 결과")
    private boolean result;
}