package com.example.demo.global.auth;


import lombok.Getter;

@Getter
public enum LoginEnumType {
    SERVICE("service"),
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String label;

    LoginEnumType(String label) {
        this.label = label;
    }
}