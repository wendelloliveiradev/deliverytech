package com.deliverytech.delivery_api.dtos;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        String category,
        BigDecimal price,
        Boolean available,
        Long restaurantId) {
}
