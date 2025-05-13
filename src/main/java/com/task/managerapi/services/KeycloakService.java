package com.task.managerapi.services;

import com.task.managerapi.dto.requests.LoginRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
    String login(LoginRequest loginRequest);

    void createUser(String username, String password);

    void createRole(String roleName);

    void assignRole(String username, String roleName);

    List<UserRepresentation> findUserByUsername(String username);
}