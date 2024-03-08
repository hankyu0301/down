package com.example.demo.domain.gethering.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "댓글 삭제 응답")
public class CommentDeleteResponseDTO {

    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    public static CommentDeleteResponseDTO of(Long id) {
        return CommentDeleteResponseDTO.builder()
                .id(id)
                .build();
    }
}