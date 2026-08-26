package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.requests.ProductRequestDto;
import com.deliverytech.delivery_api.dtos.responses.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto register(ProductRequestDto product);

    ProductResponseDto findById(Long id);

    List<ProductResponseDto> findByRestaurant(Long restaurantId);

    List<ProductResponseDto> findByCategory(String category);

    List<ProductResponseDto> findAvailableProducts();

    ProductResponseDto update(Long id, ProductRequestDto updatedProduct);

    void makeAvailable(Long id);

    void makeUnavailable(Long id);
}