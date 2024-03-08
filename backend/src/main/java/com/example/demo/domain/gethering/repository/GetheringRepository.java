package com.example.demo.domain.gethering.repository;

import com.example.demo.domain.gethering.entity.Gethering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetheringRepository extends JpaRepository<Gethering, Long> {
}