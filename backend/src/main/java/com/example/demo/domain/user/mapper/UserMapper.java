package com.example.demo.domain.user.mapper;


import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.entity.UserEntity;
import com.example.demo.domain.util.Mapper;

@Mapper
public class UserMapper {

    public UserEntity domainToEntity(User domain) {
        return UserEntity.builder()
                .id(domain.getId() == null ? null : domain.getId().getId())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .nickName(domain.getNickName())
                .userName(domain.getUserName())
                .provider(domain.getProvider())
                .gender(domain.getGender())
                .birth(domain.getBirth())
                .provider(domain.getProvider())
                .providerId(domain.getProviderId())
                .role(domain.getRole())
                .termsAgree(domain.getTermsAgree())
                .build();
    }

    public User entityToDomain(UserEntity entity) {
        return User.builder()
                .id(User.UserId.of(entity.getId()))
                .email(entity.getEmail())
                .password(entity.getPassword())
                .nickName(entity.getNickName())
                .userName(entity.getUserName())
                .gender(entity.getGender())
                .birth(entity.getBirth())
                .provider(entity.getProvider())
                .providerId(entity.getProviderId())
                .role(entity.getRole())
                .termsAgree(entity.getTermsAgree())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}