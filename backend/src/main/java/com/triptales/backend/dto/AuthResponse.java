package com.triptales.backend.dto;

public class AuthResponse {

    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(
            Long userId,
            String username,
            String email,
            String fullName,
            String message) {

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMessage() {
        return message;
    }
}