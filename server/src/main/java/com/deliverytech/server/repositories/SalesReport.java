package com.deliverytech.server.repositories;

import java.math.BigDecimal;

public interface SalesReport {
    String getRestaurantName();
    BigDecimal getTotalSales();
    Long getTotalCustomersOrders();
}
