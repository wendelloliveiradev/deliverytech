package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deliverytech.delivery_api.models.entity.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByCategory(String category);

    List<Restaurant> findByActiveTrue();

    List<Restaurant> findAllByOrderByRatingDesc();

    @Query(value = "SELECT r.name AS restaurantName, COALESCE(SUM(o.total_amount), 0) AS totalSales, COUNT(DISTINCT o.id) AS totalCustomersOrders FROM restaurants r LEFT JOIN products p ON p.restaurant_id = r.id LEFT JOIN order_item oi ON oi.product_id = p.id LEFT JOIN customers_orders o ON o.id = oi.customer_order_id GROUP BY r.id, r.name", nativeQuery = true)
    List<SalesReport> salesReportByRestaurant();
}
