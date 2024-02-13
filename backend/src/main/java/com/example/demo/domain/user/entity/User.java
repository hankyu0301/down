package com.example.demo.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키
    private String loginId;
    private String email;
    private String userName;
    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
