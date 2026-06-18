package com.naval.store.exceptions.auth;

import lombok.experimental.StandardException;

@StandardException
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(){
        super("User with this email already exist");
    }
}
