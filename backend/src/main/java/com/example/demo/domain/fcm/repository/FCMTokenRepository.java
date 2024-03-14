package com.example.demo.domain.fcm.repository;

import com.example.demo.domain.fcm.entity.FCMToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FCMTokenRepository extends CrudRepository<FCMToken, String> {

    Optional<FCMToken> findByUserId(Long userId);
}
