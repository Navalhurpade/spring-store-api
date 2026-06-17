package com.naval.store.exceptions.paymentGateway;

import lombok.experimental.StandardException;

@StandardException
public class PaymentGatewayException extends RuntimeException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
