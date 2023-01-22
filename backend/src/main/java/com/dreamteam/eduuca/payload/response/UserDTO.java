package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Role;
import com.dreamteam.eduuca.entities.User;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.Set;
import java.util.UUID;

@Log4j2
@Getter
@ToString
public class UserDTO implements ObjectDTO {
    private final UUID id;
    private final String username;
    private final String email;
    private final Set<Role> roles;

    public UserDTO(User user) {
        log.debug("new UserDTO() called. User: {}", () -> user);
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
