package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "로그아웃 응답 DTO")
public class UserLogoutResponseDTO {
    @Schema(description = "로그아웃 성공 여부", example = "true")
    private boolean success;

    @Schema(description = "로그아웃 메시지", example = "로그아웃 되었습니다.")
    private String message;

    public static UserLogoutResponseDTO of() {
        return new UserLogoutResponseDTO(true, "로그아웃 되었습니다.");
    }
}