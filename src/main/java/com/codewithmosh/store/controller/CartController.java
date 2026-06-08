package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.carts.CartDto;
import com.codewithmosh.store.dtos.carts.CartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartItemRepository;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        var newCart = Cart.builder().createdAt(LocalDateTime.now()).build();

        cartRepository.save(newCart);
        var uri = uriComponentsBuilder.path("/carts/{cartId}").buildAndExpand(newCart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartMapper.toDto(newCart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable UUID cartId) {
        var found = cartRepository.findById(cartId).orElse(null);

        if (found == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        cartRepository.delete(found);
        return ResponseEntity.noContent().build();
    }

    // Cart item apis
    @GetMapping("/{cartId}/items")
    public ResponseEntity<?> getCartItems(@PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addItemsToCart(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequest request) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        var productFound = productRepository.findById(request.getProductId()).orElse(null);
        if (productFound == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        CartItem cartItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(request.getProductId())).findFirst().orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(productFound);
            cartItem.setCart(cart);
            cartItem.setQuantity(request.getQuantity());
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID cartId, @PathVariable Long itemId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        var cartItemFound = cartItemRepository.findById(itemId).orElse(null);
        if (cartItemFound == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");

        cartItemRepository.delete(cartItemFound);

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }
}
