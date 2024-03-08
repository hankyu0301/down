package com.example.demo.domain.gethering.entity;

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
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gethering_id")
    private Gethering gethering;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "save_file_name")
    private String saveFileName;
}