package com.example.demo.domain.sports.dto.command;

import com.example.demo.domain.sports.entity.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "운동 생성 커맨드")
public class SportsCommand {
    @NotBlank
    @Schema(description = "운동 이름", example = "테니스")
    private String name;

    public Sports toEntity() {
        return Sports.builder()
                .name(name)
                .build();
    }
}