package com.example.demo.domain.gethering.entity;

import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @org.hibernate.annotations.Comment("부모 댓글")
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @org.hibernate.annotations.Comment("대댓글 작성자")
    @JoinColumn(name = "user_id")
    private User user;

    @org.hibernate.annotations.Comment("대댓글 내용")
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}