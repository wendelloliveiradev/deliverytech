package com.deliverytech.delivery_api.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

public record CustomerOrderResponseDto(
        Long id,
        LocalDateTime orderDate,
        CustomerOrderStatus status,
        Long customerId,
        BigDecimal totalAmount,
        List<OrderItemResponseDto> orderItems,
        Long version) {
}
