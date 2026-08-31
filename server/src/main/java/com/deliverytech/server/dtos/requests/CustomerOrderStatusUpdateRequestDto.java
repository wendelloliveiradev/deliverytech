package com.deliverytech.server.dtos.requests;

import com.deliverytech.server.models.enums.CustomerOrderStatus;

import jakarta.validation.constraints.NotNull;

public record CustomerOrderStatusUpdateRequestDto(
                @NotNull(message = "Status is required") CustomerOrderStatus status) {
}
