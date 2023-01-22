package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {
    private String username;
    private String email;
    private String secretQuestion;
    private String secretAnswer;
    @ToString.Exclude private String password;
    private Set<String> roles;
}
