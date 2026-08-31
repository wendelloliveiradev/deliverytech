package com.deliverytech.server.dtos.responses;

public record AuthResponseDto(String token, String tokenType, long expiresIn, UserResponseDto user) {
}