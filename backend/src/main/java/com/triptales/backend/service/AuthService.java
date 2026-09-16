package com.triptales.backend.service;

import com.triptales.backend.dto.LoginRequest;
import com.triptales.backend.dto.RegisterRequest;
import com.triptales.backend.entity.User;
import com.triptales.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Password is stored as a BCrypt hash, not plain text.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFullName(request.getFullName());
        user.setAccountStatus("ACTIVE");

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() ->
                        userRepository.findByEmail(request.getUsernameOrEmail())
                                .orElseThrow(() ->
                                        new RuntimeException("Invalid username/email or password"))
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid username/email or password");
        }

        return user;
    }
}