package com.deliverytech.delivery_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(
        @NotBlank(message = "Username is required") @Size(max = 80, message = "Username must have at most 80 characters") String username,

        @NotBlank(message = "Password is required") @Size(min = 8, max = 255, message = "Password must have between 8 and 255 characters") String password,

        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 160, message = "Email must have at most 160 characters") String email,

        @Size(max = 255, message = "Address must have at most 255 characters") String address) {
}
