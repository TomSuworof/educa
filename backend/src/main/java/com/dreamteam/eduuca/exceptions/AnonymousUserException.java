package com.dreamteam.eduuca.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AnonymousUserException extends AuthenticationException {
    private static final String NON_AUTHORIZED_USER = "[User not authorized] ";

    public AnonymousUserException(String msg, Throwable cause) {
        super(NON_AUTHORIZED_USER + msg, cause);
    }

    public AnonymousUserException(String msg) {
        super(NON_AUTHORIZED_USER + msg);
    }

    public AnonymousUserException() {
        super(NON_AUTHORIZED_USER);
    }
}
