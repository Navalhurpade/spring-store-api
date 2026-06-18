package com.naval.store.dtos.orders;

import com.naval.store.entities.PaymentStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private PaymentStatus status;
}
