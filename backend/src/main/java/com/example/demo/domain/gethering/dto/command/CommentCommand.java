package com.example.demo.domain.gethering.dto.command;

import com.example.demo.domain.gethering.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "댓글 생성 명령")
public class CommentCommand {

    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    @NotNull(message = "댓글 내용은 필수값입니다.")
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}