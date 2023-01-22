package com.dreamteam.eduuca.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RoleEnum {
    BLOCKED("delete", "blocked", new Role(0L, "ROLE_BLOCKED")),
    ADMIN("make_admin", "admin", new Role(1L, "ROLE_ADMIN")),
    USER("make_user", "user", new Role(2L, "ROLE_USER"));

    private final String actionForThisRole;
    private final String description;
    private final Role asObject; // should be used for persistence

    public static RoleEnum getFromAction(String action) {
        for (RoleEnum role : values()) {
            if (role.getActionForThisRole().equalsIgnoreCase(action)) {
                return role;
            }
        }
        throw new IllegalStateException();
    }

    public static RoleEnum getFromDescription(String description) {
        for (RoleEnum role : values()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                return role;
            }
        }
        throw new IllegalStateException();
    }
}
