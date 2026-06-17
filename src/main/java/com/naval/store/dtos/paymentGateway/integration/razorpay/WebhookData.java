package com.naval.store.dtos.paymentGateway.integration.razorpay;

public record WebhookData(
        String event,
        String razorpayOrderId,
        Long orderId,
        Long userId
) {}
