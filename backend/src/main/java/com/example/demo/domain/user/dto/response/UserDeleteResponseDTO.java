package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 삭제 응답")
public class UserDeleteResponseDTO {

    @Schema(description = "회원 삭제 결과")
    private boolean result;
}
