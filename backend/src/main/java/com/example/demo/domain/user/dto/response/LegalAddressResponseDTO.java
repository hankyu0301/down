package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 법정동 코드 응답 DTO")
public class LegalAddressResponseDTO {

    @Schema(description = "회원 id", example = "1")
    private Long userId;

    @Schema(description = "법정동 코드", example = "1111000000")
    private Long legalAddressCode;

    @Schema(description = "법정동 주소", example = "서울특별시 종로구")
    private String address;
}