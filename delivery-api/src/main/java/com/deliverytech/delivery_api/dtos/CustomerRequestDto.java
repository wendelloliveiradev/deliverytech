package com.deliverytech.delivery_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerRequestDto(
        @NotBlank(message = "Name is required") @Size(max = 120, message = "Name must have at most 120 characters") String name,

        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 160, message = "Email must have at most 160 characters") String email,

        @NotBlank(message = "Phone is required") @Size(max = 30, message = "Phone must have at most 30 characters") String phone,

        @NotBlank(message = "Address is required") @Size(max = 255, message = "Address must have at most 255 characters") String address,

        @NotNull(message = "Active flag is required") Boolean active) {
}
