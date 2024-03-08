package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.entity.EnumSportsCareer;
import com.example.demo.domain.user.entity.UserSportsInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "사용자의 운동 경력 정보 응답")
public class UserSportsInfosResponseDTO {

    @Schema(description = "운동 경력 데이터 목록")
    private List<UserSportsInfoResponseDTO> userSportsInfos;

    public static UserSportsInfosResponseDTO of(List<UserSportsInfo> userSportsInfos) {

        List<UserSportsInfoResponseDTO> dto = userSportsInfos.stream()
                .map(UserSportsInfoResponseDTO::of)
                .toList();

        return UserSportsInfosResponseDTO.builder()
                .userSportsInfos(dto)
                .build();

    }

    @Data
    @Builder
    @Schema(description = "운동 경력 데이터")
    private static class UserSportsInfoResponseDTO {
        @Schema(description = "운동 종목 ID")
        private Long sportsId;
        @Schema(description = "운동 경력")
        private EnumSportsCareer career;
        @Schema(description = "생성 시각")
        private LocalDateTime createdAt;

        public static UserSportsInfoResponseDTO of(UserSportsInfo entity) {
            return UserSportsInfoResponseDTO.builder()
                    .sportsId(entity.getSports().getId())
                    .career(entity.getCareer())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }
}