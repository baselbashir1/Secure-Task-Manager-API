package com.task.managerapi.services;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private static final String REALM = "task-manager-realm";

    public Response createUser(String username, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        return keycloak.realm(REALM).users().create(user);
    }

    public void createRole(String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        keycloak.realm(REALM).roles().create(role);
    }

    public void assignRole(String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(REALM).roles().get(roleName).toRepresentation();
        keycloak.realm(REALM).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
    }

    public List<UserRepresentation> findUserByUsername(String username) {
        return keycloak.realm(REALM).users().searchByUsername(username, true);
    }
}