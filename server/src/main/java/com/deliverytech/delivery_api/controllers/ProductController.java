package com.deliverytech.delivery_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dtos.requests.ProductRequestDto;
import com.deliverytech.delivery_api.dtos.responses.ProductResponseDto;
import com.deliverytech.delivery_api.services.interfaces.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({ "/products", "/api/products" })
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> insertProduct(@Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto response = productService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(@RequestParam(required = false) String category) {
        if (category == null || category.isBlank()) {
            return ResponseEntity.ok(productService.findAvailableProducts());
        }

        return ResponseEntity.ok(productService.findByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByRestaurant(@PathVariable @Positive Long restaurantId) {
        return ResponseEntity.ok(productService.findByRestaurant(restaurantId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable @NotBlank String category) {
        return ResponseEntity.ok(productService.findByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable @Positive Long id,
            @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<?> makeAvailable(@PathVariable @Positive Long id) {
        productService.makeAvailable(id);
        return ResponseEntity.ok("Product is now available");
    }

    @PatchMapping("/{id}/unavailable")
    public ResponseEntity<?> makeUnavailable(@PathVariable @Positive Long id) {
        productService.makeUnavailable(id);
        return ResponseEntity.ok("Product is now unavailable");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable @Positive Long id) {
        productService.makeUnavailable(id);
        return ResponseEntity.ok("Product marked as unavailable");
    }
}
