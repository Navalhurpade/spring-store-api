package com.naval.store.repositories;

import com.naval.store.entities.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findProductByCategoryId(Byte categoryId, Sort by);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p")
    List<Product> findProductWithCategory(Sort by);

        List<Product> findProductByNameOrderByCategory(String name);
}