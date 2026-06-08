package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.carts.CartDto;
import com.codewithmosh.store.dtos.carts.CartItemRequest;
import com.codewithmosh.store.dtos.carts.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private CartRepository cartRepository;
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
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
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

    // Cart item APIs
    @GetMapping("/{cartId}/items")
    public ResponseEntity<?> getCartItems(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addItemsToCart(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequest request) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        var productFound = productRepository.findById(request.getProductId()).orElse(null);
        if (productFound == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");

        var cartItem = cart.addItem(productFound);
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toCartItemDto(cartItem));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> addItemsToCart(@PathVariable UUID cartId, @PathVariable Long productId, @RequestBody @Valid UpdateCartItemRequest request) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        var productFound = productRepository.findById(productId).orElse(null);
        if (productFound == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart not found");
        }
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toCartItemDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");

        cart.removeItem(productId);
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }
}
