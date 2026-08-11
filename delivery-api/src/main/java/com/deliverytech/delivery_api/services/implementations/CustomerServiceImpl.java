package com.deliverytech.delivery_api.services.implementations;

import com.deliverytech.delivery_api.dtos.CustomerRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerResponseDto;
import com.deliverytech.delivery_api.mappers.CustomerMapper;
import com.deliverytech.delivery_api.models.entity.Customer;
import com.deliverytech.delivery_api.repositories.CustomerRepository;
import com.deliverytech.delivery_api.services.interfaces.CustomerService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDto register(CustomerRequestDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        validateCustomerData(customer);

        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "A customer with this email already exists.");
                });

        if (customer.getActive() == null) {
            customer.setActive(true);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponseDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponseDto findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with email: " + email));

        return customerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponseDto> findAllActive() {
        return customerRepository.findByActiveTrue().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponseDto update(Long id, CustomerRequestDto updatedCustomerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        Customer updatedCustomer = customerMapper.toEntity(updatedCustomerDto);

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
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setActive(updatedCustomer.getActive());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public void inactivate(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalArgumentException(
                    "Customer is already inactive.");
        }

        customer.setActive(false);

        customerRepository.save(customer);
    }

    private void validateCustomerData(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required.");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (!isValidEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
}
