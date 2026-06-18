package com.naval.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.naval.store.dtos.paymentGateway.PaymentResponse;
import com.naval.store.dtos.paymentGateway.integration.razorpay.CheckoutSession;
import com.naval.store.entities.Order;
import com.naval.store.exceptions.paymentGateway.PaymentGatewayException;
import com.razorpay.RazorpayException;

import java.util.Map;
import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order) throws JsonProcessingException;

    Optional<PaymentResponse> parseWebhookRequest(Map<String, String> headers, String payload) throws PaymentGatewayException;
}
