package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndProviderId(String email, String providerId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserName(String email, String userName);

    Optional<UserEntity> findByEmailAndUserName(String email, String username);

    Optional<UserEntity> findByEmail(String email);
}