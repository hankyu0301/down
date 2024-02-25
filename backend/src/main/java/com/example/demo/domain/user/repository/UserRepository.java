package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProviderId(String email, Long providerId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserName(String email, String userName);

    Optional<User> findByEmailAndUserName(String email, String username);

    Optional<User> findByEmail(String email);

    boolean existsByNickName(String nickName);
}