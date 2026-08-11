package com.deliverytech.delivery_api.dtos;

public record CustomerResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        Boolean active) {
}
