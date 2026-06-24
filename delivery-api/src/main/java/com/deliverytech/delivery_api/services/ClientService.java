package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Client;
import com.deliverytech.delivery_api.repositories.ClientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    /**
     * Registers a new client.
     */
    public Client register(Client client) {

        validateClientData(client);

        clientRepository.findByEmail(client.getEmail())
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "A client with this email already exists.");
                });

        if (client.getActive() == null) {
            client.setActive(true);
        }

        return clientRepository.save(client);
    }

    /**
     * Finds a client by ID.
     */
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found with ID: " + id));
    }

    /**
     * Finds a client by email.
     */
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found with email: " + email));
    }

    /**
     * Returns all active clients.
     */
    public List<Client> findAllActive() {
        return clientRepository.findByActive(true);
    }

    /**
     * Updates an existing client.
     */
    public Client update(Long id, Client updatedClient) {

        Client existingClient = findById(id);

        validateClientData(updatedClient);

        clientRepository.findByEmail(updatedClient.getEmail())
                .ifPresent(client -> {
                    if (!client.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Email is already in use by another client.");
                    }
                });

        existingClient.setName(updatedClient.getName());
        existingClient.setEmail(updatedClient.getEmail());

        return clientRepository.save(existingClient);
    }

    /**
     * Soft delete (inactivation).
     */
    public void inactivate(Long id) {

        Client client = findById(id);

        if (!Boolean.TRUE.equals(client.getActive())) {
            throw new IllegalArgumentException(
                    "Client is already inactive.");
        }

        client.setActive(false);

        clientRepository.save(client);
    }

    /**
     * Email validation.
     */
    private void validateClientData(Client client) {

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        if (client.getName() == null ||
                client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Client name is required.");
        }

        if (client.getEmail() == null ||
                client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email is required.");
        }

        if (!isValidEmail(client.getEmail())) {
            throw new IllegalArgumentException(
                    "Invalid email format.");
        }
    }

    /**
     * Basic email validation.
     */
    private boolean isValidEmail(String email) {

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email.matches(regex);
    }
}
