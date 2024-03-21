package com.example.demo.domain.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 DTO")
public class UserLoginCommand {
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "로그인 이메일은 필수 입력 값입니다.")
    @Schema(description = "로그인 이메일", example = "test1@test.com")
    private String email;

    @NotBlank(message = "로그인 비밀번호는 필수 입력 값입니다.")
    @Schema(description = "로그인 비밀번호", example = "test")
    private String password;
}