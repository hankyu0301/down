package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "대댓글 응답")
public class ReplyResponseDTO {

    @Schema(description = "대댓글 ID", example = "1")
    private Long id;

    @Schema(description = "대댓글 내용", example = "대댓글 내용입니다.")
    private String content;

    @Schema(description = "부모 댓글 ID", example = "1")
    private Long parentId;

    @Schema(description = "대댓글 닉네임", example = "1")
    private String nickname;

    public static ReplyResponseDTO of(Reply reply) {
        return ReplyResponseDTO.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .parentId(reply.getParent().getId())
                .nickname(reply.getUser().getNickName())
                .build();
    }
}