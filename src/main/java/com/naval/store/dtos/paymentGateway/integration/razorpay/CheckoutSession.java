package com.naval.store.dtos.paymentGateway.integration.razorpay;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutSession {
    private String checkoutUrl;
}
