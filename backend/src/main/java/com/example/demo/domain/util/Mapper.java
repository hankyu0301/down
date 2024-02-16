package com.example.demo.domain.util;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * 매퍼 Spring 컨포넌트 어노테이션
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Mapper {
    String value() default "";
}