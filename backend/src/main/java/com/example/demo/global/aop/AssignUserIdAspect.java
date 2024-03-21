package com.example.demo.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static com.example.demo.domain.util.AuthUtil.extractUserId;

@Aspect
@Component
@Slf4j
public class AssignUserIdAspect {

    @Before("@annotation(com.example.demo.global.aop.AssignUserId)")
    public void assignMemberId(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass(), "setUserId")
                        .ifPresent(setUserId -> invokeMethod(arg, setUserId, extractUserId())));
    }

    private Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            return Optional.of(clazz.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}