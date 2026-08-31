package com.deliverytech.delivery_api.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotBlank(message = "Name is required") @Size(min = 2, max = 50, message = "Name must have between 2 and 50 characters") String name,

        @NotBlank(message = "Category is required") @Size(max = 80, message = "Category must have at most 80 characters") String category,

        @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") @DecimalMax(value = "500.0", message = "Price must not exceed 500") BigDecimal price,

        @NotNull(message = "Available flag is required") Boolean available,

        @NotNull(message = "Restaurant id is required") Long restaurantId,

        @NotBlank(message = "Description is required") @Size(min = 10, max = 500, message = "Description must have between 10 and 500 characters") String description,

        @NotNull(message = "Stock is required") @jakarta.validation.constraints.PositiveOrZero(message = "Stock must not be negative") Integer stock) {
}
