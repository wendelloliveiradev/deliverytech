package com.deliverytech.server.services.interfaces;

import java.util.List;
import java.time.LocalDateTime;

import com.deliverytech.server.dtos.responses.CustomerOrderResponseDto;
import com.deliverytech.server.dtos.responses.CustomerResponseDto;
import com.deliverytech.server.models.enums.CustomerOrderStatus;
import com.deliverytech.server.repositories.BestSellingProductReport;
import com.deliverytech.server.repositories.SalesReport;

public interface ReportService {
    List<SalesReport> salesByRestaurant();

    List<BestSellingProductReport> bestSellingProducts();

    List<CustomerResponseDto> activeCustomers();

    List<CustomerOrderResponseDto> customerOrdersByDate(LocalDateTime start, LocalDateTime end,
            CustomerOrderStatus status);
}