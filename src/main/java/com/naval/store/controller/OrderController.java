package com.naval.store.controller;

import com.naval.store.dtos.apiResponse.ApiResponse;
import com.naval.store.dtos.orders.OrderDto;
import com.naval.store.dtos.orders.PlaceOrderRequest;
import com.naval.store.dtos.orders.UpdateOrderRequest;
import com.naval.store.mappers.OrderMapper;
import com.naval.store.services.OrderService;
import com.naval.store.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final ResponseUtils responseUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> placeOrder(
            @RequestBody @Valid PlaceOrderRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var order = orderService.placeOrder(request.getCartId());
        var orderDto = orderMapper.toDto(order);
        var uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return responseUtils.ok(orderDto, HttpStatus.CREATED, uri);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrderHistory() {
        var orders = orderService.getOrders();
        var ordersDto = orderMapper.toOrdersDto(orders);
        return responseUtils.ok(ordersDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Long orderId) {
        var order = orderService.getOrderById(orderId);
        return responseUtils.ok(orderMapper.toDto(order));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderRequest request
    ) {
        var order = orderService.updateOrder(orderId, request);
        return responseUtils.ok(orderMapper.toDto(order));
    }
}
