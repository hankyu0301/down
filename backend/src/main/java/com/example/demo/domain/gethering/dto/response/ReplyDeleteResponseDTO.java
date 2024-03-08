package com.example.demo.domain.gethering.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "대댓글 삭제 응답")
public class ReplyDeleteResponseDTO {

    @Schema(description = "삭제된 대댓글 ID", example = "1")
    private Long id;

    public static ReplyDeleteResponseDTO of(Long replyId) {
        return ReplyDeleteResponseDTO.builder()
                .id(replyId)
                .build();
    }
}