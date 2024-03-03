package com.example.demo.domain.region.dto.response;

import com.example.demo.domain.region.entity.SiDo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "시/도 응답 DTO")
public class SiDoResponseDTO {

    @Schema(description = "시/도 code", example = "11")
    private Short code;

    @Schema(description = "시/도 이름", example = "서울특별시")
    private String name;

    public static SiDoResponseDTO of(SiDo entity) {
        return SiDoResponseDTO.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }
}
