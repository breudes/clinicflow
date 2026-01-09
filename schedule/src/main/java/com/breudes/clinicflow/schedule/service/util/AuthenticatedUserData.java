package com.breudes.clinicflow.schedule.service.util;

import com.breudes.clinicflow.schedule.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class AuthenticatedUserData {
    public static User getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Optional<?> optional) {
            Object value = optional.orElse(null);
            if (value instanceof User user) {
                return user;
            }
        }
        return null;
    }
}
