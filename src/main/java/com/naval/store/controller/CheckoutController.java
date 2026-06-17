package com.naval.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.naval.store.dtos.orders.CheckoutResponse;
import com.naval.store.dtos.orders.PlaceOrderRequest;
import com.naval.store.mappers.OrderMapper;
import com.naval.store.repositories.OrderRepository;
import com.naval.store.services.CheckoutService;
import com.naval.store.services.RazorpayPaymentGateway;
import com.naval.store.utils.ResponseUtils;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RequestMapping("/checkout")
@RestController
public class CheckoutController {
    private final OrderMapper orderMapper;
    private final CheckoutService checkoutService;
    private final ResponseUtils responseUtils;
    private final OrderRepository orderRepository;
    private final RazorpayPaymentGateway razorpayPaymentGateway;

    @Transactional
    @PostMapping
    public HttpEntity<?> checkout(
            @RequestBody @Valid PlaceOrderRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var order = checkoutService.placeOrder(request.getCartId());
        try {
            var checkoutSession = razorpayPaymentGateway.createCheckoutSession(order);
            var response = new CheckoutResponse(orderMapper.toDto(order), checkoutSession.getCheckoutUrl());
            var uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();

            return responseUtils.ok(response, HttpStatus.CREATED, uri);
        } catch (RuntimeException | RazorpayException | JsonProcessingException ex) {
            orderRepository.delete(order);
            return responseUtils.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        }
    }

    @PostMapping("/webhook")
    public HttpEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        razorpayPaymentGateway.handleWebhook(payload, signature);
        return responseUtils.ok(null);
    }

}
