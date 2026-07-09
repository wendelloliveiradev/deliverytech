package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Customer;
import com.deliverytech.delivery_api.repositories.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    /**
     * Registers a new customer.
     */
    public Customer register(Customer customer) {

        validateCustomerData(customer);

        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "A customer with this email already exists.");
                });

        if (customer.getActive() == null) {
            customer.setActive(true);
        }

        return customerRepository.save(customer);
    }

    /**
     * Finds a customer by ID.
     */
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));
    }

    /**
     * Finds a customer by email.
     */
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with email: " + email));
    }

    /**
     * Returns all active customers.
     */
    public List<Customer> findAllActive() {
        return customerRepository.findByActive(true);
    }

    /**
     * Updates an existing customer.
     */
    public Customer update(Long id, Customer updatedCustomer) {

        Customer existingCustomer = findById(id);

        validateCustomerData(updatedCustomer);

        customerRepository.findByEmail(updatedCustomer.getEmail())
                .ifPresent(customer -> {
                    if (!customer.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Email is already in use by another customer.");
                    }
                });

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepository.save(existingCustomer);
    }

    /**
     * Soft delete (inactivation).
     */
    public void inactivate(Long id) {

        Customer customer = findById(id);

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalArgumentException(
                    "Customer is already inactive.");
        }

        customer.setActive(false);

        customerRepository.save(customer);
    }

    /**
     * Email validation.
     */
    private void validateCustomerData(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (customer.getName() == null ||
                customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer name is required.");
        }

        if (customer.getEmail() == null ||
                customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email is required.");
        }

        if (!isValidEmail(customer.getEmail())) {
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
