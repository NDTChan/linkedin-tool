package com.hanstack.linkedintool.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

public class AuthUtil {
    public static boolean isLogin() {
        try {
            return Objects.nonNull(getCurrentUser());
        } catch (Exception e) {
            return false;
        }

    }

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof String || Objects.isNull(authentication.getPrincipal())) {
            return null;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (StringUtils.isEmpty(userDetails.getUsername())) {
            return null;
        }

        return userDetails;
    }
}
