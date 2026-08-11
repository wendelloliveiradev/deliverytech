package com.deliverytech.delivery_api.mappers;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.dtos.CustomerRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerResponseDto;
import com.deliverytech.delivery_api.models.entity.Customer;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setActive(dto.active());
        return customer;
    }

    public CustomerResponseDto toResponse(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getActive());
    }
}
