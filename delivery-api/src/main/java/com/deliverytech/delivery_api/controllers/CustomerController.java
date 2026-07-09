package com.deliverytech.delivery_api.controllers;

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

import com.deliverytech.delivery_api.models.Customer;
import com.deliverytech.delivery_api.services.CustomerService;

import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService clientService;
    
    @PostMapping
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer saved_customer = clientService.register(customer);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved_customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering customer");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok(clientService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.findById(Long.parseLong(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        
        return ResponseEntity.ok(clientService.update(Long.parseLong(id), customer));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
        clientService.inactivate(Long.parseLong(id));

        return ResponseEntity.ok("Customer deleted successfully");
    }
    
}
