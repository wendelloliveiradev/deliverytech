package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.FoodOrder;
import com.deliverytech.delivery_api.model.StatusOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByClientId(Long clientId);

    List<FoodOrder> findByStatus(StatusOrder status);

    List<FoodOrder> findByOrderDateBetween(
            LocalDateTime start,
            LocalDateTime end);
}
