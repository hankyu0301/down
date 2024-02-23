package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.PendingEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingEmailsRepository extends JpaRepository<PendingEmail, Long> {
    boolean existsByEmail(String email);

    Optional<PendingEmail> findByEmail(String email);

    long deleteByCreatedAtBefore(LocalDateTime currentTimeMinus10);

    void deleteByEmail(String email);
}
