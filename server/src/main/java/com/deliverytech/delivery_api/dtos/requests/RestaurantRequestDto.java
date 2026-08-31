package com.deliverytech.delivery_api.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestaurantRequestDto(
        @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must have between 2 and 100 characters") String name,

        @NotBlank(message = "Category is required") @Size(max = 80, message = "Category must have at most 80 characters") String category,

        @NotNull(message = "Active flag is required") Boolean active,

        @NotNull(message = "Rating is required") @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be >= 0.0") Double rating,

        @NotBlank(message = "Address is required") @Size(max = 255, message = "Address must have at most 255 characters") String address,

        @NotBlank(message = "Phone is required") @Pattern(regexp = "^(?:\\+55\\s?)?(?:\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$", message = "Phone must be a valid Brazilian number") String phone,

        @NotNull(message = "Delivery fee is required") @DecimalMin(value = "0.0", inclusive = false, message = "Delivery fee must be greater than 0") BigDecimal deliveryFee,

        @NotBlank(message = "CEP is required") @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP must use the format 12345-678") String cep,

        @NotNull(message = "Delivery time is required") @jakarta.validation.constraints.Min(value = 10, message = "Delivery time must be at least 10 minutes") @jakarta.validation.constraints.Max(value = 120, message = "Delivery time must be at most 120 minutes") Integer deliveryTimeMinutes,

        @NotNull(message = "Latitude is required") @DecimalMin(value = "-90.0", message = "Latitude must be valid") @jakarta.validation.constraints.DecimalMax(value = "90.0", message = "Latitude must be valid") Double latitude,

        @NotNull(message = "Longitude is required") @DecimalMin(value = "-180.0", message = "Longitude must be valid") @jakarta.validation.constraints.DecimalMax(value = "180.0", message = "Longitude must be valid") Double longitude) {
}
