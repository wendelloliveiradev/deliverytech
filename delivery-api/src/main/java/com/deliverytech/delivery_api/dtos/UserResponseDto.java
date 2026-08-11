package com.deliverytech.delivery_api.dtos;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String address) {
}
