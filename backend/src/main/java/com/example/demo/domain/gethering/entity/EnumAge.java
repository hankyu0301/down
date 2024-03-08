package com.example.demo.domain.gethering.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumAge {

    TEEN((short) 10),
    TWENTIES((short) 20),
    THIRTIES((short) 30),
    FORTIES((short) 40),
    FIFTIES((short) 50),
    SIXTIES((short) 60),
    SEVENTIES((short) 70),
    EIGHTIES((short) 80),
    NINETIES((short) 90),
    HUNDREDS((short) 100),
    ALL((short) 0);

    private final Short label;
}