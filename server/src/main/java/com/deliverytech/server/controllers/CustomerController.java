package com.deliverytech.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.server.dtos.requests.CustomerRequestDto;
import com.deliverytech.server.dtos.responses.CustomerResponseDto;
import com.deliverytech.server.services.interfaces.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({ "/customers", "/api/customers" })
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody CustomerRequestDto customerDto) {
        CustomerResponseDto savedCustomer = customerService.register(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getCustomers() {
        return ResponseEntity.ok(customerService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@RequestParam @Email String email) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable @Positive Long id,
            @Valid @RequestBody CustomerRequestDto customerDto) {
        return ResponseEntity.ok(customerService.update(id, customerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable @Positive Long id) {
        customerService.deactivate(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }
}
