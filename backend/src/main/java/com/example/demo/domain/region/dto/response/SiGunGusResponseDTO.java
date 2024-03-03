package com.example.demo.domain.region.dto.response;


import com.example.demo.domain.region.entity.SiGunGu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "시/군/구 목록 응답 DTO")
public class SiGunGusResponseDTO {

    @Schema(description = "시/군/구 목록")
    List<SiGunGuResponseDTO> siGunGuList;

    public static SiGunGusResponseDTO of(List<SiGunGu> siGunGuList) {
        return SiGunGusResponseDTO.builder()
                .siGunGuList(siGunGuList.stream().map(SiGunGuResponseDTO::of).toList())
                .build();
    }
}