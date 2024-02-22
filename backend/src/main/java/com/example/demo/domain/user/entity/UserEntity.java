package com.example.demo.domain.user.entity;

import com.example.demo.domain.util.BaseEntity;
import com.example.demo.global.auth.LoginEnumType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키

    @Comment("로그인 이메일")
    @Column(name = "email", unique = true)
    private String email;

    @Comment("로그인 비밀번호")
    @Column(name = "password")
    private String password;

    @Comment("사용자 닉네임")
    @Column(name = "nick_name")
    private String nickName;

    @Comment("사용자 이름")
    @Column(name = "user_name")
    private String userName;

    @Comment("로그인 공급자 SNS 구분")
    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private LoginEnumType provider;

    @Comment("성별")
    @Column(name = "gender")
    private String gender;

    @Comment("생년월일")
    @Column(name = "birth")
    private String birth;

    @Comment("로그인 공급자 ID")
    @Column(name = "provider_id")
    private Long providerId;

    @Comment("사용자 권한")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnumType role;

    @Comment("이용약관 동의 여부")
    @Column(name = "terms_agree")
    private Boolean termsAgree;
}