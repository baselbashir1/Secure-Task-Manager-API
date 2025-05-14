package com.task.managerapi.services;

import com.task.managerapi.dto.requests.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;

    private final RestTemplate restTemplate;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.token-url}")
    private String adminTokenUrl;

    @Value("${keycloak.admin.grant_type}")
    private String grantType;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Override
    public String login(LoginRequest loginRequest) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("username", loginRequest.username());
        requestBody.add("password", loginRequest.password());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForEntity(adminTokenUrl, entity, String.class).getBody();
        log.info("User {} logged in successfully in keycloak: ", loginRequest.username());
        return response;
    }

    @Override
    public void createUser(String username, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        keycloak.realm(realm).users().create(user);
        log.info("User {} created successfully on keycloak", username);
    }

    @Override
    public void createRole(String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        keycloak.realm(realm).roles().create(role);
        log.info("Role {} created successfully on keycloak", roleName);
    }

    @Override
    public void assignRole(String username, String roleName) {
        List<UserRepresentation> users = findUserByUsername(username);
        String userId = users.get(0).getId();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
        log.info("Role {} assigned successfully to user {} on keycloak", roleName, username);
    }

    @Override
    public List<UserRepresentation> findUserByUsername(String username) {
        return keycloak.realm(realm).users().searchByUsername(username, true);
    }
}