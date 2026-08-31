package com.deliverytech.delivery_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dtos.requests.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.responses.RestaurantResponseDto;
import com.deliverytech.delivery_api.services.interfaces.RestaurantService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping({ "/restaurants", "/api/restaurants" })
@RequiredArgsConstructor
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto dto) {
        RestaurantResponseDto response = restaurantService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurants(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.ok(restaurantService.findAllActive());
        }

        return ResponseEntity.ok(restaurantService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurantsByCategory(
            @PathVariable @NotBlank String category) {
        return ResponseEntity.ok(restaurantService.findByCategory(category));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<RestaurantResponseDto>> getTopRatedRestaurants() {
        return ResponseEntity.ok(restaurantService.findTopRated());
    }

    @GetMapping("/{id}/delivery-fee/{cep}")
    public ResponseEntity<java.math.BigDecimal> calculateDeliveryFee(@PathVariable @Positive Long id,
            @PathVariable @jakarta.validation.constraints.Pattern(regexp = "^\\d{5}-?\\d{3}$") String cep) {
        return ResponseEntity.ok(restaurantService.calculateDeliveryFee(id, cep));
    }

    @GetMapping("/nearby/{cep}")
    public ResponseEntity<List<RestaurantResponseDto>> getNearbyRestaurants(
            @PathVariable @jakarta.validation.constraints.Pattern(regexp = "^\\d{5}-?\\d{3}$") String cep) {
        return ResponseEntity.ok(restaurantService.findNearby(cep));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable @Positive Long id,
            @Valid @RequestBody RestaurantRequestDto dto) {
        return ResponseEntity.ok(restaurantService.update(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateRestaurant(@PathVariable @Positive Long id) {
        restaurantService.activate(id);
        return ResponseEntity.ok("Restaurant activated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable @Positive Long id) {
        restaurantService.deactivate(id);
        return ResponseEntity.ok("Restaurant inactivated");
    }
}
