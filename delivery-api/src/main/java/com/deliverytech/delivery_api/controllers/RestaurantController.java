package com.deliverytech.delivery_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dtos.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.RestaurantResponseDto;
import com.deliverytech.delivery_api.services.interfaces.RestaurantService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequestDto dto) {
        try {
            RestaurantResponseDto response = restaurantService.register(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurants(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.ok(restaurantService.findAllActive());
        }

        return ResponseEntity.ok(restaurantService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(restaurantService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getRestaurantsByCategory(@PathVariable String category) {
        try {
            return ResponseEntity.ok(restaurantService.findByCategory(category));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<RestaurantResponseDto>> getTopRatedRestaurants() {
        return ResponseEntity.ok(restaurantService.findTopRated());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDto dto) {
        try {
            return ResponseEntity.ok(restaurantService.update(id, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateRestaurant(@PathVariable Long id) {
        try {
            restaurantService.activate(id);
            return ResponseEntity.ok("Restaurant activated");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.inactivate(id);
            return ResponseEntity.ok("Restaurant inactivated");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
