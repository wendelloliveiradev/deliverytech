package com.deliverytech.delivery_api.controllers;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.repositories.SalesReport;
import com.deliverytech.delivery_api.repositories.BestSellingProductReport;
import com.deliverytech.delivery_api.dtos.responses.CustomerResponseDto;
import com.deliverytech.delivery_api.dtos.responses.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.services.interfaces.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/reports", "/api/reports" })
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/sales-by-restaurants")
    public ResponseEntity<List<SalesReport>> salesByRestaurant() {
        return ResponseEntity.ok(reportService.salesByRestaurant());
    }

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<BestSellingProductReport>> bestSellingProducts() {
        return ResponseEntity.ok(reportService.bestSellingProducts());
    }

    @GetMapping("/active-customers")
    public ResponseEntity<List<CustomerResponseDto>> activeCustomers() {
        return ResponseEntity.ok(reportService.activeCustomers());
    }

    @GetMapping("/customer-orders-by-date")
    public ResponseEntity<List<CustomerOrderResponseDto>> customerOrdersByDate(
            @org.springframework.web.bind.annotation.RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @org.springframework.web.bind.annotation.RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @org.springframework.web.bind.annotation.RequestParam CustomerOrderStatus status) {
        return ResponseEntity.ok(reportService.customerOrdersByDate(start, end, status));
    }
}