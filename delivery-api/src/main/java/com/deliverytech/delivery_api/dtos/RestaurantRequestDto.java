package com.deliverytech.delivery_api.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRequestDto(
        @NotBlank(message = "Name is required") @Size(max = 120, message = "Name must have at most 120 characters") String name,

        @NotBlank(message = "Category is required") @Size(max = 80, message = "Category must have at most 80 characters") String category,

        @NotNull(message = "Active flag is required") Boolean active,

        @NotNull(message = "Rating is required") @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be >= 0.0") Double rating,

        @NotBlank(message = "Address is required") @Size(max = 255, message = "Address must have at most 255 characters") String address,

        @NotBlank(message = "Phone is required") @Size(max = 30, message = "Phone must have at most 30 characters") String phone,

        @NotNull(message = "Delivery fee is required") @DecimalMin(value = "0.0", inclusive = true, message = "Delivery fee must be >= 0") BigDecimal deliveryFee) {
}
