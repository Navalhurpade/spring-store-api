package com.codewithmosh.store.exceptions.cart;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@StandardException
public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cart empty.");
    }
}
