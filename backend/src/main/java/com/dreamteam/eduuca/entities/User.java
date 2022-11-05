package com.dreamteam.eduuca.entities;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User implements UserDetails {
    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Field(type = FieldType.Text)
    private String username;

    @NotBlank
    @Email
    private String email;

    @Size
    private String bio;

    @NotBlank
    private String password;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @org.springframework.data.annotation.Transient
    private String avatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !roles.contains(RoleEnum.BLOCKED.getAsObject());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    public boolean is(RoleEnum role) {
        return this.getRoles().contains(role.getAsObject());
    }
}
