package com.deliverytech.server.services.implementations;

import com.deliverytech.server.dtos.requests.CustomerRequestDto;
import com.deliverytech.server.dtos.responses.CustomerResponseDto;
import com.deliverytech.server.exceptions.BusinessException;
import com.deliverytech.server.exceptions.EntityNotFoundException;
import com.deliverytech.server.exceptions.ValidationException;
import com.deliverytech.server.mappers.CustomerMapper;
import com.deliverytech.server.models.entity.Customer;
import com.deliverytech.server.repositories.CustomerRepository;
import com.deliverytech.server.services.interfaces.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDto register(CustomerRequestDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        validateCustomerData(customer);
        ensureEmailIsUnique(customer.getEmail(), null);

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
    @Transactional
    public CustomerResponseDto update(Long id, CustomerRequestDto updatedCustomerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        Customer updatedCustomer = customerMapper.toEntity(updatedCustomerDto);

        validateCustomerData(updatedCustomer);
        ensureEmailIsUnique(updatedCustomer.getEmail(), id);

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setActive(updatedCustomer.getActive());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        if (Boolean.TRUE.equals(customer.getActive())) {
            throw new BusinessException("Customer is already active.");
        }

        customer.setActive(true);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + id));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new BusinessException("Customer is already inactive.");
        }

        customer.setActive(false);

        customerRepository.save(customer);
    }

    private void validateCustomerData(Customer customer) {
        if (customer == null) {
            throw new ValidationException("Customer cannot be null.");
        }
    }

    /** Enforces that no two different customers share the same email. */
    private void ensureEmailIsUnique(String email, Long customerIdBeingUpdated) {
        customerRepository.findByEmail(email)
                .filter(existing -> customerIdBeingUpdated == null || !existing.getId().equals(customerIdBeingUpdated))
                .ifPresent(existing -> {
                    throw new BusinessException("A customer with this email already exists.");
                });
    }
}
