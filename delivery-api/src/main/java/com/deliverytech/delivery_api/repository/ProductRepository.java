package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByRestaurantId(Long restaurantId);

    List<Product> findByCategory(String category);

    List<Product> findByAvailable(Boolean available);
}
