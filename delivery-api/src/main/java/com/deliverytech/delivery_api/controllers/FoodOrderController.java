package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class FoodOrderController {

    @PostMapping("/food-orders")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        return entity;
    }

    @GetMapping("/food-orders")
    public String getFoodOrders(@RequestParam String param) {
        return new String();
    }

    @PutMapping("/food-orders/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request

        return entity;
    }
}

