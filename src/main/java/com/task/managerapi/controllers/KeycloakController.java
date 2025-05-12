package com.task.managerapi.controllers;

import com.task.managerapi.dto.requests.LoginRequest;
import com.task.managerapi.services.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/keycloak")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KeycloakController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public ResponseEntity<?> keycloakLogin(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(keycloakService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<?> createUser(@RequestParam String username, @RequestParam String password) {
        keycloakService.createUser(username, password);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<?> createRole(@RequestParam String roleName) {
        keycloakService.createRole(roleName);
        return new ResponseEntity<>("Role created successfully", HttpStatus.OK);
    }

    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<?> assignRole(@RequestParam String username, @RequestParam String roleName) {
        keycloakService.assignRole(username, roleName);
        return new ResponseEntity<>("Role assigned successfully", HttpStatus.OK);
    }
}