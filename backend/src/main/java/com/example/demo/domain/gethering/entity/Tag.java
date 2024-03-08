package com.example.demo.domain.gethering.entity;

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
@Table(
        name = "tag",
        uniqueConstraints = {@UniqueConstraint(name = "uk_tag_name", columnNames = {"name"})}
)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("태그명")
    @Column(name = "name")
    private String name;
}