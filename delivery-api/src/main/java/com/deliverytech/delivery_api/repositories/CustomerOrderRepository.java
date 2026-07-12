package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery_api.models.CustomerOrder;
import com.deliverytech.delivery_api.utils.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomerId(Long customerId);

    @Query("SELECT DISTINCT o FROM CustomerOrder o JOIN o.orderItems oi WHERE oi.product.restaurant.id = :restaurantId")
    List<CustomerOrder> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    List<CustomerOrder> findByStatus(StatusOrder status);

    List<CustomerOrder> findByOrderDateBetween(
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT oi.product.restaurant.id, SUM(o.totalAmount) FROM CustomerOrder o JOIN o.orderItems oi GROUP BY oi.product.restaurant.id")
    List<Object[]> calcTotalSalesByRestaurant();

    @Query("SELECT c FROM CustomerOrder c WHERE c.totalAmount >= :value")
    List<CustomerOrder> findByOrdersValuesGreaterThanOrEqual(@Param("value") BigDecimal value);

    @Query("SELECT c FROM CustomerOrder c WHERE c.orderDate BETWEEN :start AND :end AND c.status = :status ORDER BY c.orderDate DESC")
    List<CustomerOrder> reportByPeriodAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
            @Param("status") StatusOrder status);
}
