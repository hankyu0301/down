package com.example.demo.domain.user.mapper;

import com.example.demo.domain.user.entity.PendingEmailsEntity;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.util.Mapper;

@Mapper
public class EmailMapper {

    public PendingEmailsEntity domainToEntity(PendingEmail domain) {
        return PendingEmailsEntity.builder()
                .id(domain.getId() == null ? null : domain.getId().getId())
                .authCode(domain.getAuthCode())
                .email(domain.getEmail())
                .authCount(domain.getAuthCount())
                .build();
    }

    public PendingEmail entityToDomain(PendingEmailsEntity entity) {
        return PendingEmail.builder()
                .id(PendingEmail.PendingEmailId.of(entity.getId()))
                .email(entity.getEmail())
                .authCode(entity.getAuthCode())
                .authCount(entity.getAuthCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
