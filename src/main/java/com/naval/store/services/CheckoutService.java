package com.naval.store.services;

import com.naval.store.entities.Order;
import com.naval.store.exceptions.cart.CartNotFoundException;
import com.naval.store.exceptions.cart.EmptyCartException;
import com.naval.store.repositories.CartRepository;
import com.naval.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthUserService userService;
    private final OrderRepository orderRepository;

    public Order placeOrder(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) throw new EmptyCartException();

        var order = Order.createFromCart(cart, userService.getCurentUser());
        orderRepository.save(order);
        return order;
    }
}
