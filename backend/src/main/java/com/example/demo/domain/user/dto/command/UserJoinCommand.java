package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청 DTO")
public class UserJoinCommand {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "로그인 이메일은 필수 입력 값입니다.")
    @Schema(description = "로그인 이메일", example = "test@gmail.com")
    private String email;

    @NotBlank(message = "로그인 비밀번호는 필수 입력 값입니다.")
    @Schema(description = "로그인 비밀번호", example = "test1234")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String nickName;

    @NotBlank(message = "이메일 인증코드는 필수 입력 값입니다.")
    @Schema(description = "이메일 인증코드", example = "1A3B5C")
    private String code;

    @NotNull(message = "이용약관 동의 여부는 필수 입력 값입니다.")
    @Schema(description = "이용약관 동의 여부", example = "true")
    private Boolean termsAgree;

    public User toUserDomain() {
        return User.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .termsAgree(termsAgree)
                .build();
    }
}