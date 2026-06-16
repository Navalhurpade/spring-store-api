package com.codewithmosh.store.exceptions;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@StandardException
public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException() {
        super("You do not have permission to access or modify this resource.");
    }
}
