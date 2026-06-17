package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.cart.CartNotFoundException;
import com.codewithmosh.store.exceptions.product.ProductNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private ProductRepository productRepository;

    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    public Cart getCart(UUID cartId) {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }

    public Cart addProductToCart(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        cart.addItem(product);
        cartRepository.save(cart);

        return cart;
    }

    public void deleteCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        cart.clearCart();

        cartRepository.save(cart);
    }

    // Cart item api
    public List<CartItem> getCartItems(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        return cart.getItems();
    }

    public CartItem updateCartItem(UUID cartId, Long productId, Integer quantity) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItem;
    }

    public Cart removeCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        cart.removeItem(productId);
        return cartRepository.save(cart);
    }
}
