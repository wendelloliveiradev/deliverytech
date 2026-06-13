package com.deliverytech.delivery_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByActive(Boolean active);
}
