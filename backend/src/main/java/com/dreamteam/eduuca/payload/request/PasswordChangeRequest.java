package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class PasswordChangeRequest {
    private UUID requestId;
    @ToString.Exclude private String newPassword;
}
