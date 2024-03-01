package com.example.demo.domain.user.dto.response;


import com.example.demo.domain.user.entity.SportsCareer;
import com.example.demo.domain.user.entity.UserSportsInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "사용자의 운동 경력 정보 응답")
public class UserSportsInfoResponseDTO {

    @Schema(description = "운동 종목 ID")
    private Long id;

    @Schema(description = "운동 경력")
    private SportsCareer career;

    public static UserSportsInfoResponseDTO of(UserSportsInfo entity) {
        return UserSportsInfoResponseDTO.builder()
                .id(entity.getSports().getId())
                .career(entity.getCareer())
                .build();
    }
}