package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponseDTO {
        @Schema(description = "태그 ID", example = "1")
        private Long id;
        @Schema(description = "태그명", example = "태그명")
        private String name;

        public static TagResponseDTO of(Tag entity) {
                return TagResponseDTO.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build();
        }
}