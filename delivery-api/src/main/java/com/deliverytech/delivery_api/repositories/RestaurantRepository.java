package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deliverytech.delivery_api.models.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByCategory(String category);

    List<Restaurant> findByActive(Boolean active);

    List<Restaurant> findAllByOrderByRatingDesc();

    @Query("SELECT r.name as nameRestaurant, SUM(p.totalAmount) as totalSales, COUNT(p.id) as quantityOrders FROM Restaurant r LEFT JOIN CustomerOrder p ON r.id = p.restaurant.id GROUP BY r.id, r.name")
    List<SalesReport> salesReportByRestaurant();
}
