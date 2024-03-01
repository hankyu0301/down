package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "사용자의 운동 경력 정보 삭제 응답")
@Builder
public class UserSportsInfoDeleteResponseDTO {

    @Schema(description = "삭제된 운동 경력 ID")
    private Long id;

    public static UserSportsInfoDeleteResponseDTO of(Long id) {
        return UserSportsInfoDeleteResponseDTO.builder()
                .id(id)
                .build();
    }
}