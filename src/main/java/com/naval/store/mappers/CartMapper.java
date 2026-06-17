package com.naval.store.mappers;

import com.naval.store.dtos.carts.CartDto;
import com.naval.store.dtos.carts.CartItemDto;
import com.naval.store.dtos.carts.CartProductDto;
import com.naval.store.entities.Cart;
import com.naval.store.entities.CartItem;
import com.naval.store.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    CartItemDto toCartItemDto(CartItem item);
    CartProductDto toCartProductDto(Product item);
    List<CartItemDto> toItemsDto(List<CartItem> items);}
