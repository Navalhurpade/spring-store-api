package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.orders.PlaceOrderRequest;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderItemId;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.EmptyCartException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest request) {
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        var order = new Order();
        var orderItems = cart.getItems().stream().map(i -> {
            var id = new OrderItemId(order.getId(), i.getProduct().getId());
            return new OrderItem(id, order, i.getProduct(), i.getQuantity(), i.getTotalPrice(), i.getProduct().getPrice());
        }).collect(Collectors.toSet());

        order.setCustomer(userService.getCurentUser());
        order.setTotalPrice(cart.getTotalPrice());
        order.setItems(orderItems);
//        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(order));
//        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
