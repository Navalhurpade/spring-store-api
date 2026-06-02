package com.codewithmosh.store.dtos.products;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private Double price;
    private Byte categoryId;

}
