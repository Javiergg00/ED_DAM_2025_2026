package com.model;

public enum UserRole {
    AUXILIAR("Auxiliar"),
    VETERINARIO("Veterinario"),
    CLIENTE("Cliente");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
