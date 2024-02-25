package com.example.demo.domain.user.dto.command;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "회원 정보 변경 요청")
public class UserInfoChangeCommand {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;
}