package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/food-orders")
public class FoodOrderController {

    @PostMapping
    public String createFoodOrder(@RequestBody String entity) {
        //TODO: process POST request
        return entity;
    }

    @GetMapping
    public String getFoodOrders(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getFoodOrderById(@PathVariable String id) {
        return new String();
    }

    @GetMapping("/{category}")
    public String getFoodOrdersByCategory(@PathVariable String category) {
        return new String();
    }
    
    @PutMapping("/{id}")
    public String updateFoodOrder(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request

        return entity;
    }

    @DeleteMapping("/{id}")
    public String deleteFoodOrder(@PathVariable String id) {
        //TODO: process DELETE request
        return new String();
    }
}

