package com.naval.store.services;

import com.naval.store.dtos.orders.UpdateOrderRequest;
import com.naval.store.entities.Order;
import com.naval.store.exceptions.cart.CartNotFoundException;
import com.naval.store.exceptions.cart.EmptyCartException;
import com.naval.store.exceptions.order.OrderNotFoundException;
import com.naval.store.exceptions.auth.ResourceForbiddenException;
import com.naval.store.repositories.CartRepository;
import com.naval.store.repositories.OrderRepository;
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
