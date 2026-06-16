package com.codewithmosh.store.dtos.orders;

import com.codewithmosh.store.entities.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private OrderStatus status;
}
