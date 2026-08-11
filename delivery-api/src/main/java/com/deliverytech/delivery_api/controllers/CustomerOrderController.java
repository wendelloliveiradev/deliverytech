package com.deliverytech.delivery_api.controllers;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dtos.CustomerOrderCreateRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerOrderStatusUpdateRequestDto;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.services.interfaces.CustomerOrderService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers-orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    @PostMapping
    public ResponseEntity<?> createCustomerOrder(@Valid @RequestBody CustomerOrderCreateRequestDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerOrderService.create(dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCustomerOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) CustomerOrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (customerId != null) {
            return ResponseEntity.ok(customerOrderService.findByCustomer(customerId));
        }

        if (status != null) {
            return ResponseEntity.ok(customerOrderService.findByStatus(status));
        }

        if (start != null && end != null) {
            return ResponseEntity.ok(customerOrderService.findByPeriod(start, end));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Provide customerId, status, or start and end query parameters");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(customerOrderService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateCustomerOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody CustomerOrderStatusUpdateRequestDto statusDto) {
        try {
            return ResponseEntity.ok(customerOrderService.changeStatus(id, statusDto.status()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(customerOrderService.cancel(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
