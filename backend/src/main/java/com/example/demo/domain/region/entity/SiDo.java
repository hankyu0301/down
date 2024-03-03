package com.example.demo.domain.region.entity;

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
@Table(name = "si_do")
public class SiDo {

    @Id
    @Column(name = "code")
    @Comment("시/도 코드 2자리")
    private Short code;

    @Comment("시/도 이름")
    @Column(name = "name")
    private String name;

    @Comment("시/군/구 코드 5자리")
    @OneToMany(mappedBy = "siDo", fetch = FetchType.LAZY)
    private List<SiGunGu> siGunGuList;
}