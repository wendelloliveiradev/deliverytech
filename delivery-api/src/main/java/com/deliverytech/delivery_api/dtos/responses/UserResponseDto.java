package com.deliverytech.delivery_api.dtos.responses;

public record UserResponseDto(
                Long id,
                String username,
                String email,
                String address) {
}
