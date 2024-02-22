package com.example.demo.domain.user.model;

import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.util.BaseDomain;
import com.example.demo.global.auth.LoginEnumType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class User extends BaseDomain {
    private UserId id; // 기본키
    private String email; // 로그인 이메일
    private String password; // 로그인 비밀번호
    private String nickName; // 사용자 이름
    private String userName; // 사용자 닉네임
    private String gender; // 성별
    private String birth; // 생년월일
    private LoginEnumType provider; // 제공자
    private Long providerId; // 제공자 ID
    private UserRoleEnumType role; // 사용자 권한
    private Boolean termsAgree; // 이용약관 동의 여부
    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class UserId {
        private Long id;
    }
}