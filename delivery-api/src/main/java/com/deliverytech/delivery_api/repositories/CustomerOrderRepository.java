package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery_api.models.entity.CustomerOrder;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

        List<CustomerOrder> findByCustomerId(Long customerId);

        @Query("SELECT DISTINCT o FROM CustomerOrder o JOIN o.orderItems oi WHERE oi.product.restaurant.id = :restaurantId")
        List<CustomerOrder> findByRestaurantId(@Param("restaurantId") Long restaurantId);

        List<CustomerOrder> findByStatus(CustomerOrderStatus status);

        @Query("SELECT c FROM CustomerOrder c WHERE c.orderDate BETWEEN :start AND :end ORDER BY c.orderDate DESC")
        List<CustomerOrder> findByOrderDateBetweenDesc(
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query("SELECT oi.product.restaurant.id, SUM(o.totalAmount) FROM CustomerOrder o JOIN o.orderItems oi GROUP BY oi.product.restaurant.id")
        List<Object[]> calcTotalSalesByRestaurant();

        @Query("SELECT c FROM CustomerOrder c WHERE c.totalAmount >= :value")
        List<CustomerOrder> findByOrdersValuesGreaterThanOrEqual(@Param("value") BigDecimal value);

        List<CustomerOrder> findTop10ByOrderByOrderDateDesc();

        @Query("""
                        SELECT DISTINCT o
                        FROM CustomerOrder o
                        LEFT JOIN FETCH o.customer
                        LEFT JOIN FETCH o.orderItems oi
                        LEFT JOIN FETCH oi.product p
                        LEFT JOIN FETCH p.restaurant
                        ORDER BY o.orderDate DESC
                        """)
        List<CustomerOrder> findAllWithDetails();

        @Query("SELECT c FROM CustomerOrder c WHERE c.orderDate BETWEEN :start AND :end AND c.status = :status ORDER BY c.orderDate DESC")
        List<CustomerOrder> reportByPeriodAndStatus(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end,
                        @Param("status") CustomerOrderStatus status);
}
