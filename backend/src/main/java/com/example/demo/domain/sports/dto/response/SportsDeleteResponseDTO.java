package com.example.demo.domain.sports.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(description = "운동 삭제 응답")
public class SportsDeleteResponseDTO {

        @Schema(description = "삭제된 운동 ID")
        private Long id;

        public static SportsDeleteResponseDTO of(Long id) {
            return SportsDeleteResponseDTO.builder()
                    .id(id)
                    .build();
        }
}