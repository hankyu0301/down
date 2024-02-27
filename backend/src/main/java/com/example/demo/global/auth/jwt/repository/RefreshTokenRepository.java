package com.example.demo.global.auth.jwt.repository;


import com.example.demo.global.auth.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByEmail(String email);

    long deleteByExpiredAtBefore(long currentTime);
}
