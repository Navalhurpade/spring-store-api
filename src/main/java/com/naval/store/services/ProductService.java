package com.naval.store.services;

import com.naval.store.dtos.products.CreateProductRequest;
import com.naval.store.dtos.products.ProductDto;
import com.naval.store.entities.Category;
import com.naval.store.entities.Product;
import com.naval.store.exceptions.product.CategoryNotFoundException;
import com.naval.store.exceptions.product.ProductNotFoundException;
import com.naval.store.mappers.ProductMapper;
import com.naval.store.repositories.CategoryRepository;
import com.naval.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<Product> getProducts(String sort, Byte categoryId) {
        if (!Set.of("name", "id", "price").contains(sort)) {
            sort = "name";
        }

        if (categoryId != null) {
            return productRepository.findProductByCategoryId(categoryId, Sort.by(sort));
        }

        return productRepository.findProductWithCategory(Sort.by(sort)).stream().toList();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        var product = productMapper.toProduct(request);
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto request) {
        var productFound = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (request.getCategoryId() != null) {
            var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
            productFound.setCategory(category);
        }

        productMapper.requestToProduct(request, productFound);
        return productRepository.save(productFound);
    }

    public void deleteProduct(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }
}
