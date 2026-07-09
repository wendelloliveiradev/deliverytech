package com.deliverytech.delivery_api.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal subtotal;
}
