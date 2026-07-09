package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery_api.models.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByActive(Boolean active);

    boolean existsByEmail(String email);

    List<Customer> findByNameContainingIgnoreCase(String name);

    Optional<Customer> findByPhone(String phone);

    @Query("SELECT DISTINCT c FROM customers c JOIN c.food_orders f WHERE c.active = true")
    List<Customer> findCustomersWithFoodOrders();

    @Query("SELECT c FROM customers c WHERE c.address LIKE CONCAT('%', :city, '%')")
    List<Customer> findByCity(@Param("city") String city);

    @Query("SELECT COUNT(c) FROM customers c WHERE c.active = true")
    Long countActiveCustomers();
}
