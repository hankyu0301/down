package com.example.demo.domain.user.entity;


import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "pending_emails",
        indexes = {
                @Index(name = "idx_pending_emails_email", columnList = "email")
        }
)
public class PendingEmailsEntity extends BaseEntity {

    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키

    @Comment("이메일")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Comment("인증 코드")
    @Column(name = "auth_code", length = 6)
    private String authCode;

    @Comment("인증 시도 횟수")
    @Column(name = "auth_count", nullable = false)
    @ColumnDefault("0")
    private Integer authCount;

}