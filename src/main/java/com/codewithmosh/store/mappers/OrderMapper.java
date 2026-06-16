package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.orders.OrderDto;
import com.codewithmosh.store.dtos.orders.OrderItemDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = ProductMapper.class)
public interface OrderMapper {
    @Mapping(source = "id", target = "orderId")
    OrderDto toDto(Order order);

    @Mapping(source = "product.id", target = "itemId")
    OrderItemDto toOrderItemDto(OrderItem order);

    List<OrderDto> toOrdersDto(List<Order> order);
}
