package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.carts.CartDto;
import com.codewithmosh.store.dtos.carts.CartItemDto;
import com.codewithmosh.store.dtos.carts.CartProductDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    CartItemDto toCartItemDto(CartItem item);
    CartProductDto toCartProductDto(Product item);
}
