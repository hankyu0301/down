package com.example.demo.domain.sports.dto.response;

import com.example.demo.domain.sports.entity.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "운동 정보 응답")
public class SportsInfoDTO {

    @Schema(description = "운동 ID")
    private Long id;

    @Schema(description = "운동 이름")
    private String name;

    public static SportsInfoDTO of(Sports sports) {
        return SportsInfoDTO.builder()
                .id(sports.getId())
                .name(sports.getName())
                .build();
    }
}