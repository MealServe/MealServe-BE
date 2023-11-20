package com.example.mealserve.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String) {
            return null; // 인증되지 않았거나, 사용자 정보를 가져올 수 없는 경우
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        return user.getUsername(); // getUsername() -> 이메일 반환
    }
}

