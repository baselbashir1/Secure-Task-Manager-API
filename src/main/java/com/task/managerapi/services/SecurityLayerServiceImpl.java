package com.task.managerapi.services;

import com.task.managerapi.dto.responses.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityLayerServiceImpl implements SecurityLayerService {

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    public UserResponse getUserFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        String username = jwt.getClaim(principleAttribute);
        String email = jwt.getClaim("email");
        return new UserResponse(userId, username, email);
    }
}