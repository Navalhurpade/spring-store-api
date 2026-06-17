package com.codewithmosh.store.exceptions;


import lombok.experimental.StandardException;

@StandardException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User with not found.");
    }
}
