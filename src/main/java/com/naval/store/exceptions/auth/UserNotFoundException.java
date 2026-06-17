package com.naval.store.exceptions.auth;


import lombok.experimental.StandardException;

@StandardException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User with not found.");
    }
}
