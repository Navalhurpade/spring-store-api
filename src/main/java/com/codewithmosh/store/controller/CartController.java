package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.carts.CartDto;
import com.codewithmosh.store.dtos.carts.CartItemDto;
import com.codewithmosh.store.dtos.carts.CartItemRequest;
import com.codewithmosh.store.dtos.carts.UpdateCartItemRequest;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.InvalidProductIdException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{cartId}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    // Cart item APIs
    @GetMapping("/{cartId}/items")
    public List<CartItemDto> getCartItems(@PathVariable UUID cartId) {
        return cartService.getCartItems(cartId);
    }

    @PostMapping("/{cartId}/items")
    public CartDto addItemsToCart(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequest request) {
        return cartService.addProductToCart(cartId, request.getProductId());
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItem(@PathVariable UUID cartId, @PathVariable Long productId, @RequestBody @Valid UpdateCartItemRequest request) {
        return cartService.updateCartItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public CartDto removeItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        return cartService.removeCartItem(cartId, productId);
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProduct() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid productId provided"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found in the cart"));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found"));
    }
}
