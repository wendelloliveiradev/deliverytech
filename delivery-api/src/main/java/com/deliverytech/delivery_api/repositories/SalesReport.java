package com.deliverytech.delivery_api.repositories;

import java.math.BigDecimal;

public interface SalesReport {
    String getRestaurantName();
    BigDecimal getTotalSales();
    Long getTotalCustomersOrders();
}
