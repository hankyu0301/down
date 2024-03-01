package com.example.demo.domain.sports.entity;

import com.example.demo.domain.user.entity.UserSportsInfo;
import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sports")
public class Sports extends BaseEntity {

    @Id
    @Comment("PK")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("운동 이름")
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "sports")
    private List<UserSportsInfo> sportsUserInfos;
}