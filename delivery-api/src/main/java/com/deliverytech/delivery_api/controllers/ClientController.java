package com.deliverytech.delivery_api.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    
    @PostMapping("/clients")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @GetMapping("/clients")
    public String getClients(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/clients/{id}")
    public String getClientById(@PathVariable String id) {
        return new String();
    }

    @PutMapping("/clients/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
    @DeleteMapping("/clients/{id}")
    public String deleteMethodName(@PathVariable String id) {
        //TODO: process DELETE request
        return new String();
    }
    
}
