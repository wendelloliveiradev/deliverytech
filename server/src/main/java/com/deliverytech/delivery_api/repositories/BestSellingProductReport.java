package com.deliverytech.delivery_api.repositories;

import java.math.BigDecimal;

public interface BestSellingProductReport {
    String getProductName();

    Long getQuantitySold();

    BigDecimal getRevenue();
}