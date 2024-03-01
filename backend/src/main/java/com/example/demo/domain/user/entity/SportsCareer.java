package com.example.demo.domain.user.entity;

import lombok.Getter;

@Getter
public enum SportsCareer {
    BEGINNER("입문자"),
    NOVICE("초보자"),
    INTERMEDIATE("중급자"),
    MASTER("마스터"),
    VETERAN("고인물"),
    GOD("신");

    private final String career;

    SportsCareer(String career) {
        this.career = career;
    }
}