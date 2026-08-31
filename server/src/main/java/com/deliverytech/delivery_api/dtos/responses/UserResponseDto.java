package com.deliverytech.delivery_api.dtos.responses;

import com.deliverytech.delivery_api.models.enums.UserRole;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String address,
        UserRole role) {
}
