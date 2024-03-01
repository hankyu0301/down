package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.UserSportsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSportsInfoRepository extends JpaRepository<UserSportsInfo, Long> {
    List<UserSportsInfo> findByUserId(Long id);

    Optional<UserSportsInfo> findByUserIdAndSportsId(Long userId, Long sportsId);
}