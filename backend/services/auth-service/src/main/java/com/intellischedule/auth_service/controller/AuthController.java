package com.intellischedule.auth_service.controller;

import com.intellischedule.auth_service.service.AuthService;
import com.intellischedule.auth_service.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request
    ) {

        User user = authService.register(
                request.username(),
                request.email(),
                request.password(),
                request.role()
        );

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        String token = authService.login(
                request.username(),
                request.password()
        );

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }

    @GetMapping("/me")
public ResponseEntity<User> currentUser(
        Authentication authentication
) {

    String username =
            authentication.getName();

    User user =
            authService.getCurrentUser(username);

    return ResponseEntity.ok(user);
}

    public record RegisterRequest(
            String username,
            String email,
            String password,
            String role
    ) {
    }

    public record LoginRequest(
            String username,
            String password
    ) {
    }

    public record LoginResponse(
            String token
    ) {
    }
}