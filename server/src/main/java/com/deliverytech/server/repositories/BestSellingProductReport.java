package com.deliverytech.server.repositories;

import java.math.BigDecimal;

public interface BestSellingProductReport {
    String getProductName();

    Long getQuantitySold();

    BigDecimal getRevenue();
}