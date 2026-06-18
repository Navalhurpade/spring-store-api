package com.naval.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naval.store.dtos.paymentGateway.PaymentResponse;
import com.naval.store.dtos.paymentGateway.integration.razorpay.CheckoutSession;
import com.naval.store.dtos.paymentGateway.integration.razorpay.RazorpayPayload;
import com.naval.store.dtos.paymentGateway.integration.razorpay.WebhookData;
import com.naval.store.entities.Order;
import com.naval.store.entities.PaymentStatus;
import com.naval.store.exceptions.paymentGateway.InvalidWebhookSignatureException;
import com.naval.store.exceptions.paymentGateway.PaymentGatewayException;
import com.naval.store.repositories.OrderRepository;
import com.naval.store.utils.AppUtils;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentGateway implements PaymentGateway {
    private final AppUtils appUtils;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    @Value("${razorpay.api-key}")
    private String razorpayApiKey;

    @Value("${razorpay.api-secret}")
    private String razorpayApiSecret;

    @Value("${razorpay.webhook-secret}")
    private String webhookSignature;

    @Value("${server.website-url}")
    private String websiteUrl;


    @Override
    public CheckoutSession createCheckoutSession(Order order) throws JsonProcessingException {
        try {
            var razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);
            var payload = createPaymentLinkPayload(order);
            String jsonString = objectMapper.writeValueAsString(payload);

            var data = razorpay.paymentLink.create(new JSONObject(jsonString));
            var checkoutUrl = data.get("short_url").toString();

            return new CheckoutSession(checkoutUrl);
        } catch (Exception e) {
            throw new PaymentGatewayException(e);
        }
    }

    @Override
    public Optional<PaymentResponse> parseWebhookRequest(Map<String, String> headers, String jsonPayload) throws
            PaymentGatewayException {
        var signature = headers.get("x-razorpay-signature");
        try {
            boolean isValid = Utils.verifyWebhookSignature(jsonPayload, signature, webhookSignature);
            if (!isValid) throw new InvalidWebhookSignatureException();
            var payload = extractDataFromPayload(jsonPayload);

            return switch (payload.event()) {
                case "payment_link.paid" -> Optional.of(new PaymentResponse(payload.orderId(), PaymentStatus.PAID));
                case "payment_link.cancelled" ->
                        Optional.of(new PaymentResponse(payload.orderId(), PaymentStatus.FAILED));
                case "payment.failed" ->
                        Optional.of(new PaymentResponse(payload.orderId(), PaymentStatus.FAILED));
                default -> Optional.empty();
            };
        } catch (Exception e) {
            throw new PaymentGatewayException("Failed to parse the webhook request");
        }
    }

    private WebhookData extractDataFromPayload(String jsonPayload) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(jsonPayload);
        JsonNode orderEntity = root.path("payload").path("order").path("entity");

        return new WebhookData(root.path("event").asText(), orderEntity.path("id").asText(), orderEntity.path("notes").path("orderId").asLong(), orderEntity.path("notes").path("userId").asLong());
    }

    private RazorpayPayload createPaymentLinkPayload(Order order) {
        return new RazorpayPayload(new RazorpayPayload.RazorpayCustomer(order.getCustomer().getName(), order.getCustomer().getEmail()), new RazorpayPayload.RazorpayNotes(order.getCustomer().getId().toString(), order.getId().toString()), order.getId().toString(), appUtils.getAmountInPaisa(order.getTotalPrice()), "INR", websiteUrl + "/chekout/webhook", "get");
    }
}
