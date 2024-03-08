package com.example.demo.domain.gethering.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "태그 삭제 응답")
public class TagDeleteResponseDTO {

        @Schema(description = "삭제된 태그 ID")
        private Long id;

        public static TagDeleteResponseDTO of(Long id) {
            return TagDeleteResponseDTO.builder()
                    .id(id)
                    .build();
        }
}