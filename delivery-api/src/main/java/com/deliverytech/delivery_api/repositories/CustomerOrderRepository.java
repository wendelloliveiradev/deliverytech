package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.models.CustomerOrder;
import com.deliverytech.delivery_api.utils.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomerId(Long customerId);

    List<CustomerOrder> findByRestaurantId(Long restaurantId);

    List<CustomerOrder> findByStatus(StatusOrder status);

    List<CustomerOrder> findByOrderDateBetween(
            LocalDateTime start,
            LocalDateTime end);
}
