package com.naval.store.controller;

import com.naval.store.dtos.apiResponse.ApiResponse;
import com.naval.store.dtos.products.CreateProductRequest;
import com.naval.store.dtos.products.ProductDto;
import com.naval.store.mappers.ProductMapper;
import com.naval.store.services.ProductService;
import com.naval.store.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ResponseUtils responseUtils;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(
            @RequestParam(name = "sort", defaultValue = "", required = false) String sort,
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        var data = productService.getProducts(sort, categoryId);
        var products = data.stream().map(productMapper::toDto).collect(Collectors.toList());
        return responseUtils.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        var product = productService.getProductById(id);
        return responseUtils.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestBody @Valid CreateProductRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var newProduct = productService.createProduct(request);
        var productDto = productMapper.toDto(newProduct);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return responseUtils.ok(productDto, HttpStatus.CREATED, uri);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @RequestBody ProductDto request,
            @PathVariable Long id
    ) {
        var updatedProduct = productService.updateProduct(id, request);
        return responseUtils.ok(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return responseUtils.ok(null, HttpStatus.NO_CONTENT, null);
    }
}
