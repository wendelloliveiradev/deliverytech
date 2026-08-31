package com.deliverytech.delivery_api.dtos.responses;

public record AuthResponseDto(String token, String tokenType, long expiresIn, UserResponseDto user) {
}