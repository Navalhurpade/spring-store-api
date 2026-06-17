package com.naval.store.dtos.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponse {
    OrderDto order;
    String paymentLink;
}
