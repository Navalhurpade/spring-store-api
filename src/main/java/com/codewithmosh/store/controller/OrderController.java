package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.orders.OrderDto;
import com.codewithmosh.store.dtos.orders.PlaceOrderRequest;
import com.codewithmosh.store.dtos.orders.UpdateOrderRequest;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.services.OrderService;
import com.codewithmosh.store.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderRequest request) {
        var order = orderService.placeOrder(request.getCartId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrderHistory() {
        var order = orderService.getOrders();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrdersDto(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        var order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long orderId, @RequestBody UpdateOrderRequest request) {
        var order = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }
}
