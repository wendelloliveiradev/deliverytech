package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByActive(Boolean active);
}
