package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.models.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByCategory(String category);

    List<Restaurant> findByActive(Boolean active);

    List<Restaurant> findAllByOrderByRatingDesc();
}
