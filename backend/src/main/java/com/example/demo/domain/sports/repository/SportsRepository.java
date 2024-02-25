package com.example.demo.domain.sports.repository;

import com.example.demo.domain.sports.entity.Sports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsRepository extends JpaRepository<Sports, Long> {
    boolean existsByName(String name);
}
