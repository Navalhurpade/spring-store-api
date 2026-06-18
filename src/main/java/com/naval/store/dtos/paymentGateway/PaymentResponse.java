package com.naval.store.dtos.paymentGateway;

import com.naval.store.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResponse {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
