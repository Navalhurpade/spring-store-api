package com.naval.store.dtos.orders;

import com.naval.store.entities.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private OrderStatus status;
}
