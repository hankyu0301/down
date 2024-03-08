package com.example.demo.global.auth;


import lombok.Getter;

@Getter
public enum EnumLoginType {
    SERVICE("service"),
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String label;

    EnumLoginType(String label) {
        this.label = label;
    }
}