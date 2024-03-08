package com.example.demo.domain.gethering.entity;


import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @org.hibernate.annotations.Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.hibernate.annotations.Comment("모임")
    @ManyToOne
    @JoinColumn(name = "gethering_id")
    private Gethering gethering;

    @org.hibernate.annotations.Comment("사용자")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @org.hibernate.annotations.Comment("댓글 내용")
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @org.hibernate.annotations.Comment("대댓글")
    @OneToMany(mappedBy = "parent")
    private List<Reply> children;
}