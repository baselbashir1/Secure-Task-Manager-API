package com.task.managerapi.controllers;

import com.task.managerapi.services.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/keycloak")
@RequiredArgsConstructor
public class KeycloakAdminController {

    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        Response response = keycloakAdminService.createUser(username, password);
        return ResponseEntity.status(response.getStatus()).body("User created");
    }

    @PostMapping("/roles")
    public ResponseEntity<String> createRole(@RequestParam String roleName) {
        keycloakAdminService.createRole(roleName);
        return ResponseEntity.ok("Role created");
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam String roleName) {
        List<UserRepresentation> users = keycloakAdminService.findUserByUsername(username);
        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        String userId = users.get(0).getId();
        keycloakAdminService.assignRole(userId, roleName);
        return ResponseEntity.ok("Role assigned");
    }
}