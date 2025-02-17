package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    private final Map<String, String> roleRedirectMap;

    public SuccessUserHandler() {
        roleRedirectMap = new HashMap<>();
        roleRedirectMap.put("ROLE_ADMIN", "/users");
        roleRedirectMap.put("ROLE_USER", "/users");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/users");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/users");
        } else {
            httpServletResponse.sendRedirect("/");
        }
    }
}