package com.example.demo.domain.region.dto.response;

import com.example.demo.domain.region.entity.SiDo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "시/도 목록 응답 DTO")
public class SiDosResponseDTO {

    @Schema(description = "시/도 목록")
    List<SiDoResponseDTO> siDoList;

    public static SiDosResponseDTO of(List<SiDo> siDoList) {
        return SiDosResponseDTO.builder()
                .siDoList(siDoList.stream().map(SiDoResponseDTO::of).toList())
                .build();
    }
}