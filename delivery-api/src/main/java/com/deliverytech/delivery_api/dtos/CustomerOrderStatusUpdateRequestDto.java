package com.deliverytech.delivery_api.dtos;

import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

import jakarta.validation.constraints.NotNull;

public record CustomerOrderStatusUpdateRequestDto(
        @NotNull(message = "Status is required") CustomerOrderStatus status) {
}
