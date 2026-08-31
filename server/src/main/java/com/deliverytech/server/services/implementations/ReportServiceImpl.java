package com.deliverytech.server.services.implementations;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.server.repositories.RestaurantRepository;
import com.deliverytech.server.repositories.CustomerRepository;
import com.deliverytech.server.repositories.CustomerOrderRepository;
import com.deliverytech.server.repositories.BestSellingProductReport;
import com.deliverytech.server.repositories.SalesReport;
import com.deliverytech.server.dtos.responses.CustomerResponseDto;
import com.deliverytech.server.dtos.responses.CustomerOrderResponseDto;
import com.deliverytech.server.mappers.CustomerMapper;
import com.deliverytech.server.mappers.CustomerOrderMapper;
import com.deliverytech.server.models.enums.CustomerOrderStatus;
import com.deliverytech.server.services.interfaces.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;

    @Override
    public List<SalesReport> salesByRestaurant() {
        return restaurantRepository.salesReportByRestaurant();
    }

    @Override
    public List<BestSellingProductReport> bestSellingProducts() {
        return customerOrderRepository.bestSellingProducts();
    }

    @Override
    public List<CustomerResponseDto> activeCustomers() {
        return customerRepository.findCustomersWithFoodOrders().stream().map(customerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerOrderResponseDto> customerOrdersByDate(LocalDateTime start, LocalDateTime end,
            CustomerOrderStatus status) {
        return customerOrderRepository.reportByPeriodAndStatus(start, end, status).stream()
                .map(customerOrderMapper::toResponse).toList();
    }
}