package com.example.demo.domain.user.entity;


import com.example.demo.domain.sports.entity.Sports;
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
@Table(name = "user_sports_info")
public class UserSportsInfo extends BaseEntity {

    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("사용자 ID")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("운동 ID")
    @JoinColumn(name = "sports_id")
    private Sports sports;

    @Comment("운동 경력")
    @Column(name = "career")
    @Enumerated(EnumType.ORDINAL)
    private EnumSportsCareer career;
}