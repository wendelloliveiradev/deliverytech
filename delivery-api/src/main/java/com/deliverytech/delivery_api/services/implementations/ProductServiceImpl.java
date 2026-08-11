package com.deliverytech.delivery_api.services.implementations;

import com.deliverytech.delivery_api.dtos.ProductRequestDto;
import com.deliverytech.delivery_api.dtos.ProductResponseDto;
import com.deliverytech.delivery_api.mappers.ProductMapper;
import com.deliverytech.delivery_api.models.entity.Product;
import com.deliverytech.delivery_api.models.entity.Restaurant;
import com.deliverytech.delivery_api.repositories.ProductRepository;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;
import com.deliverytech.delivery_api.services.interfaces.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto register(ProductRequestDto productDto) {
        Restaurant restaurant = validateRestaurant(productDto.restaurantId());
        Product product = productMapper.toEntity(productDto, restaurant);

        validateProductData(product);

        if (product.getAvailable() == null) {
            product.setAvailable(true);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponseDto> findByRestaurant(Long restaurantId) {
        validateRestaurant(restaurantId);
        return productRepository.findByRestaurantId(restaurantId).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }

        return productRepository.findByCategory(category).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findAvailableProducts() {
        return productRepository.findByAvailableTrue().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto updatedProductDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        Restaurant restaurant = validateRestaurant(updatedProductDto.restaurantId());
        Product updatedProduct = productMapper.toEntity(updatedProductDto, restaurant);

        validateProductData(updatedProduct);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setAvailable(updatedProduct.getAvailable());
        existingProduct.setRestaurant(restaurant);

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public void makeUnavailable(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalArgumentException("Product is already unavailable.");
        }

        product.setAvailable(false);
        productRepository.save(product);
    }

    @Override
    public void makeAvailable(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        if (Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalArgumentException("Product is already available.");
        }

        product.setAvailable(true);
        productRepository.save(product);
    }

    private void validateProductData(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required.");
        }

        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Product price is required.");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }

        if (product.getRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant is required.");
        }

        if (product.getRestaurant().getId() == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }
    }

    private Restaurant validateRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + restaurantId));
    }
}