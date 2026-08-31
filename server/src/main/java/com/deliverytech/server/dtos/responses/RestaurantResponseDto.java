package com.deliverytech.server.dtos.responses;

import java.math.BigDecimal;

public record RestaurantResponseDto(
        Long id,
        String name,
        String category,
        Boolean active,
        Double rating,
        String address,
        String phone,
        BigDecimal deliveryFee,
        String cep,
        Integer deliveryTimeMinutes,
        Double latitude,
        Double longitude) {
    public RestaurantResponseDto(Long id, String name, String category, Boolean active, Double rating, String address,
            String phone, BigDecimal deliveryFee) {
        this(id, name, category, active, rating, address, phone, deliveryFee, null, null, null, null);
    }
}
