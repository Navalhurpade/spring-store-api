package com.naval.store.controller;

import com.naval.store.dtos.apiResponse.ApiResponse;
import com.naval.store.dtos.carts.CartDto;
import com.naval.store.dtos.carts.CartItemDto;
import com.naval.store.dtos.carts.CartItemRequest;
import com.naval.store.dtos.carts.UpdateCartItemRequest;
import com.naval.store.entities.Cart;
import com.naval.store.exceptions.product.ProductNotFoundException;
import com.naval.store.mappers.CartMapper;
import com.naval.store.services.CartService;
import com.naval.store.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final ResponseUtils responseUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<Cart>> createCart(UriComponentsBuilder uriComponentsBuilder) {
        var cart = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{cartId}").buildAndExpand(cart.getId()).toUri();

        return responseUtils.ok(cart, HttpStatus.CREATED, uri);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse<CartDto>> getCart(@PathVariable UUID cartId) {
        var cartDto = cartMapper.toDto(cartService.getCart(cartId));

        return responseUtils.ok(cartDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);

        return responseUtils.ok(null, HttpStatus.NO_CONTENT, null);
    }

    // Cart item APIs
    @GetMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<List<CartItemDto>>> getCartItems(@PathVariable UUID cartId) {
        var cartItemsDto = cartMapper.toItemsDto(cartService.getCartItems(cartId));

        return responseUtils.ok(cartItemsDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<CartDto>> addItemsToCart(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequest request) {
        var cartDto = cartMapper.toDto(cartService.addProductToCart(cartId, request.getProductId()));

        return responseUtils.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartItemDto>> updateCartItem(@PathVariable UUID cartId, @PathVariable Long productId, @RequestBody @Valid UpdateCartItemRequest request) {
        var cartItemDto = cartMapper.toCartItemDto(cartService.updateCartItem(cartId, productId, request.getQuantity()));

        return responseUtils.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        var cartDto = cartMapper.toDto(cartService.removeCartItem(cartId, productId));

        return responseUtils.ok(cartDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFound() {
        return responseUtils.error("Product not found in the cart", HttpStatus.NOT_FOUND, null);
    }
}
