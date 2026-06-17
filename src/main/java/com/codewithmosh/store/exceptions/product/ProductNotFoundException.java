package com.codewithmosh.store.exceptions.product;

import lombok.experimental.StandardException;

@StandardException
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product Not found.");
    }
}
