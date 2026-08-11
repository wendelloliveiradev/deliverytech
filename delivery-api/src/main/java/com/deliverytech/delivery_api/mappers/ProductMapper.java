package com.deliverytech.delivery_api.mappers;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.dtos.ProductRequestDto;
import com.deliverytech.delivery_api.dtos.ProductResponseDto;
import com.deliverytech.delivery_api.models.entity.Product;
import com.deliverytech.delivery_api.models.entity.Restaurant;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto, Restaurant restaurant) {
        Product product = new Product();
        product.setName(dto.name());
        product.setCategory(dto.category());
        product.setPrice(dto.price());
        product.setAvailable(dto.available());
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
                restaurantId);
    }
}
