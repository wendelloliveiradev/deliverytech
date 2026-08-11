package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.CustomerRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto register(CustomerRequestDto customer);

    CustomerResponseDto findById(Long id);

    CustomerResponseDto findByEmail(String email);

    List<CustomerResponseDto> findAllActive();

    CustomerResponseDto update(Long id, CustomerRequestDto updatedCustomer);

    void inactivate(Long id);
}
