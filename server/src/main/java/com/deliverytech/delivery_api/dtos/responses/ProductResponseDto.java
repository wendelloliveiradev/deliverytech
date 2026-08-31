package com.deliverytech.delivery_api.dtos.responses;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        String category,
        BigDecimal price,
        Boolean available,
        Long restaurantId,
        String description,
        Integer stock) {
    public ProductResponseDto(Long id, String name, String category, BigDecimal price, Boolean available,
            Long restaurantId) {
        this(id, name, category, price, available, restaurantId, null, null);
    }
}
