package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.orders.UpdateOrderRequest;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderItemId;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.EmptyCartException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.exceptions.ResourceForbiddenException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public Order placeOrder(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

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
