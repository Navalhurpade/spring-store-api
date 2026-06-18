package com.naval.store.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<OrderItem> items;

    private BigDecimal totalPrice;

    @JsonManagedReference
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public static Order createFromCart(Cart cart, User customer) {
        var order = new Order();
        var orderItems = cart.getItems().stream().map(i -> {
            var id = new OrderItemId(order.getId(), i.getProduct().getId());
            return new OrderItem(id, order, i.getProduct(), i.getQuantity(), i.getTotalPrice(), i.getProduct().getPrice());
        }).collect(Collectors.toSet());

        order.setCustomer(customer);
        order.setTotalPrice(cart.getTotalPrice());
        order.setItems(orderItems);
        return order;
    }
}