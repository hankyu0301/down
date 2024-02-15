package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.PendingEmailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingEmailsRepository extends JpaRepository<PendingEmailsEntity, Long> {
    boolean existsByEmail(String email);

    Optional<PendingEmailsEntity> findByEmail(String email);
}
