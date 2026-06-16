package com.codewithmosh.store.dtos.orders;

import com.codewithmosh.store.dtos.users.UserDto;
import com.codewithmosh.store.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private UserDto customer;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
    private Date createdAt;
}
