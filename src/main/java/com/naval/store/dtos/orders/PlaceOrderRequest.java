package com.naval.store.dtos.orders;

import lombok.Data;

import java.util.UUID;

@Data
public class PlaceOrderRequest {
    private UUID cartId;
}
