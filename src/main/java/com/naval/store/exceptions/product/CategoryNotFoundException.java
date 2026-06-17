package com.naval.store.exceptions.product;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Provided category not found");
    }
}
