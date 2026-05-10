package com.model;

public class SessionUser {
    private final String username;
    private final UserRole role;

    public SessionUser(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
