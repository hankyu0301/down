package com.example.demo.global.auth.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "access_token")
public class AccessToken {

    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Comment("인증 토큰")
    @Column(name = "access_token")
    private String accessToken;

    @Comment("이메일")
    @Column(name = "email")
    private String email;

    @Comment("인증 토큰 만료일")
    @Column(name = "token_expire_at")
    private long expiredAt;

    @Comment("사용가능 여부")
    @Column(name = "is_usable")
    private boolean isUsable;
}
