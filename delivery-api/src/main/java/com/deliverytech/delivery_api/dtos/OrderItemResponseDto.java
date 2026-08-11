package com.deliverytech.delivery_api.dtos;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long id,
        Long productId,
        String productName,
        int quantity,
        BigDecimal subtotal) {
}
