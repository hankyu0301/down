package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 정보 조회 응답")
public class UserInfoResponseDTO {

    @Schema(description = "회원 번호", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;

    @Schema(description = "닉네임", example = "페이커")
    private String nickName;

    @Schema(description = "이름", example = "이상혁")
    private String userName;

    public static UserInfoResponseDTO from(User user) {
        return UserInfoResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .userName(user.getUserName())
                .build();
    }
}