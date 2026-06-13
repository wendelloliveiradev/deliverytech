package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.models.FoodOrder;
import com.deliverytech.delivery_api.models.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByClientId(Long clientId);

    List<FoodOrder> findByStatus(StatusOrder status);

    List<FoodOrder> findByOrderDateBetween(
            LocalDateTime start,
            LocalDateTime end);
}
