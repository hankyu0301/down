package com.example.demo.domain.gethering.dto.command;

import com.example.demo.domain.gethering.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "태그 생성 요청")
public class TagCommand {
    @NotBlank(message = "태그명을 입력해주세요.")
    @Schema(description = "태그명", example = "태그명")
    private String name;

    public Tag toEntity() {
        return Tag.builder()
                .name(name)
                .build();
    }
}
