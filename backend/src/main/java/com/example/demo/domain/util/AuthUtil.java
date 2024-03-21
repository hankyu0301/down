package com.example.demo.domain.util;

import com.example.demo.domain.user.entity.EnumUserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtil {

    public static boolean isAuthenticated() {
        return getAuthentication() != null &&
                getAuthentication().isAuthenticated();
    }

    public static Long extractUserId() {
        return (Long) getAuthentication().getPrincipal();
    }

    public static Set<EnumUserRole> extractEnumUserRole() {
        return getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(EnumUserRole::valueOf)
                .collect(Collectors.toSet());
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}