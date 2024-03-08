package com.example.demo.domain.gethering.repository;

import com.example.demo.domain.gethering.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
