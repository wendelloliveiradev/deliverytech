package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Customer;
import com.deliverytech.delivery_api.models.CustomerOrder;
import com.deliverytech.delivery_api.repositories.CustomerRepository;
import com.deliverytech.delivery_api.repositories.CustomerOrderRepository;
import com.deliverytech.delivery_api.utils.StatusOrder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;

    /**
     * Creates a new order.
     */
    @Transactional
    public CustomerOrder create(CustomerOrder order) {

        validateOrder(order);

        Customer customer = validateCustomer(
                order.getCustomer().getId());

        order.setCustomer(customer);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() == null) {
            order.setStatus(StatusOrder.CONFIRMED);
        }

        return customerOrderRepository.save(order);
    }

    /**
     * Finds an order by ID.
     */
    public CustomerOrder findById(Long id) {

        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with ID: " + id));
    }

    /**
     * Finds orders by customer.
     */
    public List<CustomerOrder> findByCustomer(Long customerId) {

        validateCustomer(customerId);

        return customerOrderRepository.findByCustomerId(customerId);
    }

    /**
     * Finds orders by status.
     */
    public List<CustomerOrder> findByStatus(StatusOrder status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status is required.");
        }

        return customerOrderRepository.findByStatus(status);
    }

    /**
     * Finds orders within a period.
     */
    public List<CustomerOrder> findByPeriod(
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

        return customerOrderRepository.findByOrderDateBetween(
                start,
                end);
    }

    /**
     * Changes order status.
     */
    public CustomerOrder changeStatus(
            Long orderId,
            StatusOrder newStatus) {

        CustomerOrder order = findById(orderId);

        validateStatusTransition(
                order.getStatus(),
                newStatus);

        order.setStatus(newStatus);

        return customerOrderRepository.save(order);
    }

    /**
     * Cancels an order.
     */
    public CustomerOrder cancel(Long orderId) {

        return changeStatus(
                orderId,
                StatusOrder.CANCELLED);
    }

    /**
     * Validates order data.
     */
    private void validateOrder(CustomerOrder order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        if (order.getCustomer() == null) {
            throw new IllegalArgumentException(
                    "Customer is required.");
        }

        if (order.getCustomer().getId() == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required.");
        }
    }

    /**
     * Validates that the customer exists and is active.
     */
    private Customer validateCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: "
                                + customerId));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalArgumentException(
                    "Inactive customers cannot place orders.");
        }

        return customer;
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