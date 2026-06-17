package com.naval.store.dtos.orders;

import com.naval.store.dtos.users.UserDto;
import com.naval.store.entities.OrderStatus;
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
