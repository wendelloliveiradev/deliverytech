package com.deliverytech.delivery_api.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CustomerOrderCreateRequestDto(
        @NotNull(message = "Customer id is required") Long customerId,

        @NotEmpty(message = "Order must have at least one item") List<@Valid OrderItemRequestDto> orderItems) {
}
