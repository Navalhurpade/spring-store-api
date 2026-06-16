package com.codewithmosh.store.dtos.orders;

import lombok.Data;

import java.util.UUID;

@Data
public class PlaceOrderRequest {
    private UUID cartId;
}
