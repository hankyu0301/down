package com.example.demo.domain.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "비밀번호 변경 요청")
public class PasswordChangeCommand {
    @Schema(description = "신규 비밀번호", example = "tset1234!")
    private String password;
}