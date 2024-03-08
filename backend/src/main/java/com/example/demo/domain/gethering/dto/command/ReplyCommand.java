package com.example.demo.domain.gethering.dto.command;

import com.example.demo.domain.gethering.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "대댓글 등록")
public class ReplyCommand {

    @Schema(description = "대댓글 내용", example = "대댓글 내용입니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public Reply toEntity() {
        return Reply.builder()
                .content(content)
                .build();
    }
}
