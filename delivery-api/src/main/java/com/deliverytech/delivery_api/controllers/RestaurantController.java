package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @PostMapping
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

    @GetMapping
    public String getRestaurants(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getRestaurantById(@PathVariable String id) {
        return new String();
    }

    @GetMapping("/{category}")
    public String getRestaurantsByCategory(@PathVariable String category) {
        return new String();
    }

    @PutMapping("/{id}")
    public String updateRestaurant(@PathVariable String id, @RequestBody String entity) {
        // TODO: process PUT request

        return entity;
    }

    @DeleteMapping("/{id}")
    public String deleteRestaurant(@PathVariable String id) {

        return new String();
    }

}
