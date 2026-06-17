package com.naval.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naval.store.dtos.paymentGateway.integration.razorpay.CheckoutSession;
import com.naval.store.dtos.paymentGateway.integration.razorpay.RazorpayPayload;
import com.naval.store.entities.Order;
import com.naval.store.utils.AppUtils;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentGateway implements PaymentGateway {
    private final AppUtils appUtils;
    private final ObjectMapper objectMapper;

    @Value("${razorpay.api-key}")
    private String razorpayApiKey;

    @Value("${razorpay.api-secret}")
    private String razorpayApiSecret;

    @Value("${server.website-url}")
    private String websiteUrl;


    @Override
    public CheckoutSession createCheckoutSession(Order order) throws RazorpayException, JsonProcessingException {
        var razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);
        var payload = createPaymentLinkPayload(order);
        String jsonString = objectMapper.writeValueAsString(payload);

        var data = razorpay.paymentLink.create(new JSONObject(jsonString));
        var checkoutUrl = data.get("short_url").toString();

        return new CheckoutSession(checkoutUrl);
    }

    private RazorpayPayload createPaymentLinkPayload(Order order) {
        return new RazorpayPayload(
                new RazorpayPayload.RazorpayCustomer(order.getCustomer().getName(), order.getCustomer().getEmail()),
                new RazorpayPayload.RazorpayNotes(order.getCustomer().getId().toString(), order.getId().toString()),
                order.getId().toString(),
                appUtils.getAmountInPaisa(order.getTotalPrice()),
                "INR",
                websiteUrl + "/chekout/webhook",
                "get"
        );
    }
}
