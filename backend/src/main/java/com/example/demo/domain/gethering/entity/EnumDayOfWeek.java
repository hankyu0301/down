package com.example.demo.domain.gethering.entity;

import java.util.ArrayList;
import java.util.List;

public enum EnumDayOfWeek {
    MONDAY("월", 0b0000001),
    TUESDAY("화", 0b0000010),
    WEDNESDAY("수", 0b0000100),
    THURSDAY("목", 0b0001000),
    FRIDAY("금", 0b0010000),
    SATURDAY("토", 0b0100000),
    SUNDAY("일", 0b1000000);
    private final String label;
    private final int bit;

    EnumDayOfWeek(String label, int bit) {
        this.label = label;
        this.bit = bit;
    }

    public static Integer of(List<String> daysOfWeek) {
        int result = 0;
        for (String dayOfWeek : daysOfWeek) {
            for (EnumDayOfWeek enumDayOfWeek : EnumDayOfWeek.values()) {
                if (!enumDayOfWeek.label.equals(dayOfWeek)) {
                    throw new IllegalArgumentException("해당하는 이 없습니다.");
                }
                result |= enumDayOfWeek.bit;
            }
        }
        return result;
    }

    public static List<String> of(Integer daysOfWeek) {
        List<String> result = new ArrayList<>();
        for (EnumDayOfWeek enumDayOfWeek : EnumDayOfWeek.values()) {
            if ((daysOfWeek & enumDayOfWeek.bit) != 0) {
                result.add(enumDayOfWeek.label);
            }
        }
        return result;
    }

    public static EnumDayOfWeek of(String label) {
        for (EnumDayOfWeek dayOfWeek : EnumDayOfWeek.values()) {
            if (dayOfWeek.label.equals(label)) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("해당하는 이 없습니다.");
    }
}