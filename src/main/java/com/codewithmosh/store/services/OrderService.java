package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.orders.UpdateOrderRequest;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.cart.CartNotFoundException;
import com.codewithmosh.store.exceptions.cart.EmptyCartException;
import com.codewithmosh.store.exceptions.order.OrderNotFoundException;
import com.codewithmosh.store.exceptions.auth.ResourceForbiddenException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {
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

    public List<Order> getOrders() {
        var user = userService.getCurentUser();
        return orderRepository.getOrderByCustomer_Id(user.getId());
    }

    public Order getOrderById(Long orderId) {
        var user = userService.getCurentUser();
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (!Objects.equals(order.getCustomer().getId(), user.getId())) {
            throw new ResourceForbiddenException();
        }
        return order;
    }

    public Order updateOrder(Long orderId, UpdateOrderRequest request) {
        var user = userService.getCurentUser();
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!Objects.equals(order.getCustomer().getId(), user.getId())) {
            throw new ResourceForbiddenException();
        }

        order.setStatus(request.getStatus());
        orderRepository.save(order);
        return order;
    }
}
