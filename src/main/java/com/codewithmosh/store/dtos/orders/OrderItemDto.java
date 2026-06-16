package com.codewithmosh.store.dtos.orders;

import com.codewithmosh.store.dtos.products.ProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long itemId;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
