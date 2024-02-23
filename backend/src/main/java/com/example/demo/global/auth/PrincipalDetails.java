package com.example.demo.global.auth;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.example.demo.domain.user.entity.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User userEntity;
    private final Map<String, Object> attributes;

    @Override
    public String getName() {
        return userEntity.getUserName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.<GrantedAuthority>asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
    }

    public String getEmail() {
        return userEntity.getEmail();
    }

    public Long getId() {
        return userEntity.getId();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public User toEntity() {
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .nickName(userEntity.getNickName())
                .userName(userEntity.getUserName())
                .provider(userEntity.getProvider())
                .providerId(userEntity.getProviderId())
                .role(userEntity.getRole())
                .termsAgree(userEntity.getTermsAgree())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }
}