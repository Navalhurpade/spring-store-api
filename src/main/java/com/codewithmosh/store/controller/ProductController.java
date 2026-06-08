package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.products.CreateProductRequest;
import com.codewithmosh.store.dtos.products.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    Iterable<ProductDto> getProducts(
            @RequestParam(name = "sort", defaultValue = "", required = false) String sort,
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        List<Product> data;
        if (!Set.of("name", "id", "price").contains(sort)) {
            sort = "name";
        }

        if (categoryId != null) {
            data = productRepository.findProductByCategoryId(categoryId, Sort.by(sort));
        } else {
            data = productRepository.findProductWithCategory(Sort.by(sort)).stream().toList();
        }

        return data.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var data = productRepository.findById(id).orElse(null);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(data));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody @Valid CreateProductRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var data = productMapper.toProduct(request);
        data.setCategory(category);

        var newProduct = productRepository.save(data);

        var productDto = productMapper.toDto(newProduct);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @RequestBody ProductDto request,
            @PathVariable Long id
    ) {
        var productFound = productRepository.findById(id).orElse(null);
        if (productFound == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getCategoryId() != null) {
            var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().build();
            }
            productFound.setCategory(category);
        }


        productMapper.requestToProduct(request, productFound);
        productRepository.save(productFound);

        return ResponseEntity.ok(productMapper.toDto(productFound));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        var data = productRepository.findById(id).orElse(null);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(data);

        return ResponseEntity.noContent().build();
    }
}
