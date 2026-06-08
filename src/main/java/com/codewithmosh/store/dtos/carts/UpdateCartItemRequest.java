package com.codewithmosh.store.dtos.carts;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than 1")
    @Max(value = 10, message = "Quantity must be less than 10")
    public Integer quantity;
}
