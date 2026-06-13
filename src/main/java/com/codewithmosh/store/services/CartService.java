package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.carts.CartDto;
import com.codewithmosh.store.dtos.carts.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.InvalidProductIdException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = cartRepository.save(new Cart());
        return cartMapper.toDto(cart);
    }

    public CartDto getCart(UUID cartId) {
        var cart = validateAndGetCart(cartId);
        return cartMapper.toDto(cart);
    }

    public CartDto addProductToCart(UUID cartId, Long productId) {
        var cart = validateAndGetCart(cartId);
        var product = validateAndGetProduct(productId);

        cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public void deleteCart(UUID cartId) {
        var cart = validateAndGetCart(cartId);

        cart.clearCart();
        cartRepository.save(cart);
    }

    // Cart item api
    public List<CartItemDto> getCartItems(UUID cartId) {
        var cart = validateAndGetCart(cartId);

        return cartMapper.toItemsDto(cart.getItems());
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity) {
        var cart = validateAndGetCart(cartId);
        validateAndGetProduct(productId);

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toCartItemDto(cartItem);
    }

    public CartDto removeCartItem(UUID cartId, Long productId) {
        var cart = validateAndGetCart(cartId);
        cart.removeItem(productId);
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @NonNull
    private Cart validateAndGetCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cart;
    }

    @NotNull
    private Product validateAndGetProduct(Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new InvalidProductIdException();
        }
        return product;
    }
}
