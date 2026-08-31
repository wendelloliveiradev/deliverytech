package com.deliverytech.server.mappers;

import org.springframework.stereotype.Component;

import com.deliverytech.server.dtos.requests.ProductRequestDto;
import com.deliverytech.server.dtos.responses.ProductResponseDto;
import com.deliverytech.server.models.entity.Product;
import com.deliverytech.server.models.entity.Restaurant;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto, Restaurant restaurant) {
        Product product = new Product();
        product.setName(dto.name());
        product.setCategory(dto.category());
        product.setPrice(dto.price());
        product.setAvailable(dto.available());
        product.setDescription(dto.description());
        product.setStock(dto.stock());
        product.setRestaurant(restaurant);
        return product;
    }

    public ProductResponseDto toResponse(Product product) {
        Long restaurantId = product.getRestaurant() != null ? product.getRestaurant().getId() : null;

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getAvailable(),
                restaurantId,
                product.getDescription(),
                product.getStock());
    }
}
