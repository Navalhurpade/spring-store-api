package com.codewithmosh.store.dtos.carts;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CartDto {
    private UUID id;
    private Set<CartItemDto> items;
    private LocalDateTime createdAt;
}
