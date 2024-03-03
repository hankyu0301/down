package com.example.demo.domain.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 법정동 코드 등록")
public class LegalAddressCommand {

    @Schema(description = "법정동 코드", example = "2917000000")
    @Digits(integer = 10, fraction = 0, message = "법정동 코드는 10자리 숫자입니다.")
    private Long legalAddressCode;
}