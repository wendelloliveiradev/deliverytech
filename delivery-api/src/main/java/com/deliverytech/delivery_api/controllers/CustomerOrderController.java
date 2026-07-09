package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/customers-orders")
public class CustomerOrderController {

    @PostMapping
    public String createCustomerOrder(@RequestBody String entity) {
        //TODO: process POST request
        return entity;
    }

    @GetMapping
    public String getCustomerOrders(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getCustomerOrderById(@PathVariable String id) {
        return new String();
    }

    @GetMapping("/{category}")
    public String getCustomerOrdersByCategory(@PathVariable String category) {
        return new String();
    }
    
    @PutMapping("/{id}")
    public String updateCustomerOrder(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request

        return entity;
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerOrder(@PathVariable String id) {
        //TODO: process DELETE request
        return new String();
    }
}

