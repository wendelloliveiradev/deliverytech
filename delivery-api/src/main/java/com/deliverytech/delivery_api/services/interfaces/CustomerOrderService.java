package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.CustomerOrderCreateRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderService {
    CustomerOrderResponseDto create(CustomerOrderCreateRequestDto order);

    CustomerOrderResponseDto findById(Long id);

    List<CustomerOrderResponseDto> findByCustomer(Long customerId);

    List<CustomerOrderResponseDto> findByStatus(CustomerOrderStatus status);

    List<CustomerOrderResponseDto> findByPeriod(LocalDateTime start, LocalDateTime end);

    CustomerOrderResponseDto changeStatus(Long orderId, CustomerOrderStatus newStatus);

    CustomerOrderResponseDto cancel(Long orderId);
}