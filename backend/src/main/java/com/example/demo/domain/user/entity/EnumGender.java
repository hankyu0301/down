package com.example.demo.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumGender {
    // 남자
    MAN("남자"),
    // 여자
    WOMAN("여자"),
    // 기타
    OTHER("기타"),
    // 혼성
    MIXED("혼성");

    private final String label;

    public static EnumGender of(String label) {
        for (EnumGender gender : EnumGender.values()) {
            if (gender.label.equals(label)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("해당하는 스포츠 경력이 없습니다.");
    }
}