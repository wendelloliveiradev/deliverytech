package com.deliverytech.delivery_api.dtos.responses;

import java.math.BigDecimal;

public record RestaurantResponseDto(
                Long id,
                String name,
                String category,
                Boolean active,
                Double rating,
                String address,
                String phone,
                BigDecimal deliveryFee) {
}
