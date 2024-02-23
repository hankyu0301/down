package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "회원가입 여부 확인 요청")
public class UserJoinCheckCommand {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "회원가입 여부 확인 이메일", example = "test@gmail.com")
    private String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Schema(description = "회원가입 여부 확인 이름", example = "홍길동")
    private String userName;
}