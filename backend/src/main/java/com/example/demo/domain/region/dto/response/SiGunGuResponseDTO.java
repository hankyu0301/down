package com.example.demo.domain.region.dto.response;

import com.example.demo.domain.region.entity.SiGunGu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "시/군/구 응답 DTO")
public class SiGunGuResponseDTO {

    @Schema(description = "시/군/구 ID")
    private Short code;

    @Schema(description = "시/군/구 이름")
    private String name;

    @Schema(description = "법정동 코드")
    private Long legalAddressCode;

    public static SiGunGuResponseDTO of(SiGunGu entity) {
        return SiGunGuResponseDTO.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .legalAddressCode(entity.getLegalAddressCode())
                .build();
    }
}