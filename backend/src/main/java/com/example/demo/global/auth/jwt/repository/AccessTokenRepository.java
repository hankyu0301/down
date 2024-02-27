package com.example.demo.global.auth.jwt.repository;

import com.example.demo.global.auth.jwt.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    List<AccessToken> findAllByEmail(String email);

    long deleteByExpiredAtBefore(long currentTime);
}
