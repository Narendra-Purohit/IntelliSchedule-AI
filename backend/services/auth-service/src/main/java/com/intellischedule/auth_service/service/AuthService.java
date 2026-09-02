package com.intellischedule.auth_service.service;

import com.intellischedule.auth_service.entity.User;
import com.intellischedule.auth_service.repository.UserRepository;
import com.intellischedule.auth_service.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(
            String username,
            String email,
            String password,
            String role
    ) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(
                role == null || role.isBlank()
                        ? "USER"
                        : role
        );

        return userRepository.save(user);
    }

    public String login(
            String username,
            String password
    ) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        return jwtService.generateToken(user);
    }

    public User getCurrentUser(String username) {

    return userRepository
            .findByUsername(username)
            .orElseThrow(
                    () -> new RuntimeException(
                            "User not found"
                    )
            );
}
}