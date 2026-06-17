package com.naval.store.repositories;


import com.naval.store.entities.Cart;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    public Optional<Cart> getCartWithItems(@PathParam("cartId") UUID cartId);
}
