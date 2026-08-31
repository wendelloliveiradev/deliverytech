package com.deliverytech.server.dtos.responses;

import java.math.BigDecimal;

public record OrderItemResponseDto(
                Long id,
                Long productId,
                String productName,
                int quantity,
                BigDecimal subtotal) {
}
