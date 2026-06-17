package com.naval.store.dtos.carts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequest {

    @NotNull(message = "Product id is required")
    @Positive(message = "Product id must be a positive number")
    private Long productId;
}
