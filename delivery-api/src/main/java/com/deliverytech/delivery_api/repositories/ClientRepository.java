package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery_api.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByActive(Boolean active);

    boolean existsByEmail(String email);

    List<Client> findByNameContainingIgnoreCase(String name);

    Optional<Client> findByPhone(String phone);

    @Query("SELECT DISTINCT c FROM clients c JOIN c.food_orders f WHERE c.active = true")
    List<Client> findClientsWithFoodOrders();

    @Query("SELECT c FROM clients c WHERE c.address LIKE CONCAT('%', :city, '%')")
    List<Client> findByCity(@Param("city") String city);

    @Query("SELECT COUNT(c) FROM clients c WHERE c.active = true")
    Long countActiveClients();
}
