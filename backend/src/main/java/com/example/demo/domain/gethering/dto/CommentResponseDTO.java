package com.example.demo.domain.gethering.dto;

import com.example.demo.domain.gethering.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "댓글 응답")
public class CommentResponseDTO {

    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    private String content;
    @Schema(description = "모임 ID", example = "1")
    private Long getheringId;
    @Schema(description = "사용자 닉네임", example = "페이커")
    private String nickName;
    @Schema(description = "생성일시", example = "2021-07-01T00:00:00")
    private LocalDateTime createdAt;

    public static CommentResponseDTO of(Comment entity) {
        return CommentResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .getheringId(entity.getGethering().getId())
                .nickName(entity.getUser().getNickName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}