package com.naval.store.dtos.paymentGateway.integration.razorpay;

public record RazorpayPayload(
        RazorpayCustomer customer,
        RazorpayNotes notes,
        String reference_id,
        long amount,
        String currency,
        String callback_url,
        String callback_method
) {
    public record RazorpayCustomer(String name, String email) {
    }

    public record RazorpayNotes(String userId, String orderId) {
    }
}


