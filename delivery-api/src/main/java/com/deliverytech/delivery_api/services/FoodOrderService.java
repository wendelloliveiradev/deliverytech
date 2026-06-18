package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Client;
import com.deliverytech.delivery_api.models.FoodOrder;
import com.deliverytech.delivery_api.repositories.ClientRepository;
import com.deliverytech.delivery_api.repositories.FoodOrderRepository;
import com.deliverytech.delivery_api.utils.StatusOrder;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final ClientRepository clientRepository;

    public FoodOrderService(
            FoodOrderRepository foodOrderRepository,
            ClientRepository clientRepository) {

        this.foodOrderRepository = foodOrderRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Creates a new order.
     */
    public FoodOrder create(FoodOrder order) {

        validateOrder(order);

        Client client = validateClient(
                order.getClient().getId());

        order.setClient(client);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() == null) {
            order.setStatus(StatusOrder.CONFIRMED);
        }

        return foodOrderRepository.save(order);
    }

    /**
     * Finds an order by ID.
     */
    public FoodOrder findById(Long id) {

        return foodOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with ID: " + id));
    }

    /**
     * Finds orders by client.
     */
    public List<FoodOrder> findByClient(Long clientId) {

        validateClient(clientId);

        return foodOrderRepository.findByClientId(clientId);
    }

    /**
     * Finds orders by status.
     */
    public List<FoodOrder> findByStatus(StatusOrder status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status is required.");
        }

        return foodOrderRepository.findByStatus(status);
    }

    /**
     * Finds orders within a period.
     */
    public List<FoodOrder> findByPeriod(
            LocalDateTime start,
            LocalDateTime end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException(
                    "Start and end dates are required.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
        }

        return foodOrderRepository.findByOrderDateBetween(
                start,
                end);
    }

    /**
     * Changes order status.
     */
    public FoodOrder changeStatus(
            Long orderId,
            StatusOrder newStatus) {

        FoodOrder order = findById(orderId);

        validateStatusTransition(
                order.getStatus(),
                newStatus);

        order.setStatus(newStatus);

        return foodOrderRepository.save(order);
    }

    /**
     * Cancels an order.
     */
    public FoodOrder cancel(Long orderId) {

        return changeStatus(
                orderId,
                StatusOrder.CANCELLED);
    }

    /**
     * Validates order data.
     */
    private void validateOrder(FoodOrder order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        if (order.getClient() == null) {
            throw new IllegalArgumentException(
                    "Client is required.");
        }

        if (order.getClient().getId() == null) {
            throw new IllegalArgumentException(
                    "Client ID is required.");
        }
    }

    /**
     * Validates that the client exists and is active.
     */
    private Client validateClient(Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found with ID: "
                                + clientId));

        if (!Boolean.TRUE.equals(client.getActive())) {
            throw new IllegalArgumentException(
                    "Inactive clients cannot place orders.");
        }

        return client;
    }

    /**
     * Validates status transitions.
     */
    private void validateStatusTransition(
            StatusOrder current,
            StatusOrder next) {

        if (next == null) {
            throw new IllegalArgumentException(
                    "New status is required.");
        }

        if (current == StatusOrder.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cancelled orders cannot be modified.");
        }

        if (current == StatusOrder.DELIVERED) {
            throw new IllegalArgumentException(
                    "Delivered orders cannot be modified.");
        }
    }
}