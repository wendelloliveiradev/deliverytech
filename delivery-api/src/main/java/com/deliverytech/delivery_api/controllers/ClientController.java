package com.deliverytech.delivery_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery_api.models.Client;
import com.deliverytech.delivery_api.services.ClientService;

import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    
    @PostMapping
    public ResponseEntity<?> registerClient(@Valid @RequestBody Client client) {
        try {
            Client saved_client = clientService.register(client);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved_client);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering client");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.findById(Long.parseLong(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody Client client) {
        
        return ResponseEntity.ok(clientService.update(Long.parseLong(id), client));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable String id) {
        clientService.inactivate(Long.parseLong(id));

        return ResponseEntity.ok("Client deleted successfully");
    }
    
}
