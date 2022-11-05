package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Role;
import com.dreamteam.eduuca.entities.User;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class UserDTO implements ObjectDTO {
    private final UUID id;
    private final String username;
    private final String email;
    private final Set<Role> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
