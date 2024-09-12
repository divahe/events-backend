package com.example.events.exceptions;

public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException(String message) {
        super(message);
    }
}
