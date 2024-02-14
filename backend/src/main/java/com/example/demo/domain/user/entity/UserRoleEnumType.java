package com.example.demo.domain.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnumType {
    ROLE_USER("일반 사용자"),
    ROLE_INSTRUCTOR("강사"),
    ROLE_ADMIN("관리자");

    final String label;
}
