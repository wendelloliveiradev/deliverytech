package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.ProductRequestDto;
import com.deliverytech.delivery_api.dtos.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto register(ProductRequestDto product);

    ProductResponseDto findById(Long id);

    List<ProductResponseDto> findByRestaurant(Long restaurantId);

    List<ProductResponseDto> findByCategory(String category);

    List<ProductResponseDto> findAvailableProducts();

    ProductResponseDto update(Long id, ProductRequestDto updatedProduct);

    void makeUnavailable(Long id);

    void makeAvailable(Long id);
}