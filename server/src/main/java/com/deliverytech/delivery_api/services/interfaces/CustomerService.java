package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.requests.CustomerRequestDto;
import com.deliverytech.delivery_api.dtos.responses.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto register(CustomerRequestDto customer);

    CustomerResponseDto findById(Long id);

    CustomerResponseDto findByEmail(String email);

    List<CustomerResponseDto> findAllActive();

    CustomerResponseDto update(Long id, CustomerRequestDto updatedCustomer);

    void activate(Long id);

    void deactivate(Long id);
}
