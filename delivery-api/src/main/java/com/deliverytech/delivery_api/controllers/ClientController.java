package com.deliverytech.delivery_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {
    
    @PostMapping
    public ResponseEntity<?> postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public String getClients(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getClientById(@PathVariable String id) {
        return new String();
    }

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
    @DeleteMapping("/{id}")
    public String deleteMethodName(@PathVariable String id) {
        //TODO: process DELETE request
        return new String();
    }
    
}
