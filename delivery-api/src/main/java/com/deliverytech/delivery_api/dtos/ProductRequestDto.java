package com.deliverytech.delivery_api.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotBlank(message = "Name is required") @Size(max = 120, message = "Name must have at most 120 characters") String name,

        @NotBlank(message = "Category is required") @Size(max = 80, message = "Category must have at most 80 characters") String category,

        @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal price,

        @NotNull(message = "Available flag is required") Boolean available,

        @NotNull(message = "Restaurant id is required") Long restaurantId) {
}
