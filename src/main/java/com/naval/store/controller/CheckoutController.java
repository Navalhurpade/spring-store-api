package com.naval.store.controller;

import com.naval.store.dtos.apiResponse.ApiResponse;
import com.naval.store.dtos.orders.OrderDto;
import com.naval.store.dtos.orders.PlaceOrderRequest;
import com.naval.store.mappers.OrderMapper;
import com.naval.store.services.CheckoutService;
import com.naval.store.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RequestMapping("/checkout")
@Service
public class CheckoutController {
    private final OrderMapper orderMapper;
    private final CheckoutService checkoutService;
    private final ResponseUtils responseUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> checkout(
            @RequestBody @Valid PlaceOrderRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var order = checkoutService.placeOrder(request.getCartId());
        var orderDto = orderMapper.toDto(order);
        var uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return responseUtils.ok(orderDto, HttpStatus.CREATED, uri);
    }
}
