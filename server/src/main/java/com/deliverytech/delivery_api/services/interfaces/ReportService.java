package com.deliverytech.delivery_api.services.interfaces;

import java.util.List;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.dtos.responses.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.dtos.responses.CustomerResponseDto;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.repositories.BestSellingProductReport;
import com.deliverytech.delivery_api.repositories.SalesReport;

public interface ReportService {
    List<SalesReport> salesByRestaurant();

    List<BestSellingProductReport> bestSellingProducts();

    List<CustomerResponseDto> activeCustomers();

    List<CustomerOrderResponseDto> customerOrdersByDate(LocalDateTime start, LocalDateTime end,
            CustomerOrderStatus status);
}