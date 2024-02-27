package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
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

    @NotBlank(message = "사용자 닉네임은 필수 입력 값입니다.")
    @Schema(description = "사용자 닉네임", example = "페이커")
    private String nickName;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    @Schema(description = "회원가입 여부 확인 성별", example = "male, female, other")
    private String gender;

    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    @Schema(description = "회원가입 여부 확인 생년월일", example = "9999-99-99")
    private String birth;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    @Schema(description = "사용자 이름", example = "이상혁")
    private String userName;

    @NotBlank(message = "이메일 인증코드는 필수 입력 값입니다.")
    @Schema(description = "이메일 인증코드", example = "1A3B5C")
    private String code;

    @NotNull(message = "이용약관 동의 여부는 필수 입력 값입니다.")
    @Schema(description = "이용약관 동의 여부", example = "true")
    private Boolean termsAgree;

    @AssertTrue(message = "이용약관에 동의해야 합니다.")
    private boolean isTermsAgree() {
        return termsAgree != null && termsAgree;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .birth(birth)
                .gender(gender)
                .userName(userName)
                .termsAgree(termsAgree)
                .build();
    }
}