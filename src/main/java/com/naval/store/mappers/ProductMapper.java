package com.naval.store.mappers;


import com.naval.store.dtos.products.CreateProductRequest;
import com.naval.store.dtos.products.ProductDto;
import com.naval.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    Product toProduct(CreateProductRequest request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    void requestToProduct(
            ProductDto request,
            @MappingTarget Product productToUpdate
    );
}
