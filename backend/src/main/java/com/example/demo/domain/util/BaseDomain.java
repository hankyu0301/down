package com.example.demo.domain.util;


import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class BaseDomain {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}