package com.deliverytech.delivery_api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(
        @NotNull(message = "Product id is required") Long productId,

        @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
}
