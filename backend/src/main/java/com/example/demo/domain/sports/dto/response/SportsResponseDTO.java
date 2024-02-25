package com.example.demo.domain.sports.dto.response;

import com.example.demo.domain.sports.entity.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(description = "운동 응답")
public class SportsResponseDTO {

        @Schema(description = "운동 ID")
        private Long id;

        @Schema(description = "운동 이름")
        private String name;

        public static SportsResponseDTO of(Sports sports) {
            return SportsResponseDTO.builder()
                    .id(sports.getId())
                    .name(sports.getName())
                    .build();
        }
}