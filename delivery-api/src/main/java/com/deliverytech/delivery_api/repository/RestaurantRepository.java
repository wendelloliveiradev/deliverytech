package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByCategory(String category);

    List<Restaurant> findByActive(Boolean active);

    List<Restaurant> findAllByOrderByRatingDesc();
}
