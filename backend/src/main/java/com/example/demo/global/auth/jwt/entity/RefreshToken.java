package com.example.demo.global.auth.jwt.entity;

import com.example.demo.domain.util.BaseEntity;
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
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Comment("재발급 토큰")
    @Column(name = "refresh_token")
    private String refreshToken;

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