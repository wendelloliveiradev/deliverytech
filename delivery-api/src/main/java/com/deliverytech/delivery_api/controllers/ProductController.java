package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/products")
public class ProductController {
    @PostMapping
    public String insertProduct(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @GetMapping
    public String getProducts(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable String id) {
        return new String();
    }
    
    @GetMapping("/{restaurant}")
    public String getProductsByRestaurant(@PathVariable String restaurant) {
        return new String();
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        return entity;
    }
    
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        //TODO: process DELETE request
        return new String();
    }
}
