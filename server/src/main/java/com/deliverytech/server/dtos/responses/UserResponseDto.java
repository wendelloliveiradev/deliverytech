package com.deliverytech.server.dtos.responses;

import com.deliverytech.server.models.enums.UserRole;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String address,
        UserRole role) {
}
