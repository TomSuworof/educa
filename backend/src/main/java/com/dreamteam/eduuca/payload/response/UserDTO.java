package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.User;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Getter
@ToString
public class UserDTO implements ObjectDTO {
    private final UUID id;
    private final String username;
    private final String email;
    private final Set<String> roles;

    public UserDTO(User user) {
        log.debug("new UserDTO() called. User: {}", () -> user);
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
