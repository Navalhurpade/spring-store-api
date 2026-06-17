package com.naval.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.naval.store.dtos.paymentGateway.integration.razorpay.CheckoutSession;
import com.naval.store.entities.Order;
import com.razorpay.RazorpayException;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order) throws RazorpayException, JsonProcessingException;
}
