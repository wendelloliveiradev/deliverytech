package com.deliverytech.server.services.implementations;

import com.deliverytech.server.dtos.requests.ProductRequestDto;
import com.deliverytech.server.dtos.responses.ProductResponseDto;
import com.deliverytech.server.exceptions.BusinessException;
import com.deliverytech.server.exceptions.EntityNotFoundException;
import com.deliverytech.server.exceptions.ValidationException;
import com.deliverytech.server.mappers.ProductMapper;
import com.deliverytech.server.models.entity.Product;
import com.deliverytech.server.models.entity.Restaurant;
import com.deliverytech.server.repositories.ProductRepository;
import com.deliverytech.server.repositories.RestaurantRepository;
import com.deliverytech.server.services.interfaces.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "productLists", allEntries = true)
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
    @Cacheable(cacheNames = "products", key = "#id")
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    @Cacheable(cacheNames = "productLists", key = "'restaurant:' + #restaurantId")
    public List<ProductResponseDto> findByRestaurant(Long restaurantId) {
        validateRestaurant(restaurantId);
        return productRepository.findByRestaurantId(restaurantId).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "productLists", key = "'category:' + #category.toLowerCase()")
    public List<ProductResponseDto> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("Category is required.");
        }

        return productRepository.findByCategory(category).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "productLists", key = "'available'")
    public List<ProductResponseDto> findAvailableProducts() {
        return productRepository.findByAvailableTrue().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "products", key = "#id"),
            @CacheEvict(cacheNames = "productLists", allEntries = true)
    })
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
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setRestaurant(restaurant);

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "products", key = "#id"),
            @CacheEvict(cacheNames = "productLists", allEntries = true)
    })
    public void makeAvailable(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        if (Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessException("Product is already available.");
        }

        product.setAvailable(true);

        productRepository.save(product);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "products", key = "#id"),
            @CacheEvict(cacheNames = "productLists", allEntries = true)
    })
    public void makeUnavailable(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessException("Product is already unavailable.");
        }

        product.setAvailable(false);

        productRepository.save(product);
    }

    private void validateProductData(Product product) {
        if (product == null) {
            throw new ValidationException("Product cannot be null.");
        }

        if (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Product price must be greater than zero.");
        }

        if (product.getRestaurant() == null) {
            throw new ValidationException("Restaurant is required.");
        }

        if (product.getRestaurant().getId() == null) {
            throw new ValidationException("Restaurant ID is required.");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new ValidationException("Product stock must not be negative.");
        }
    }

    private Restaurant validateRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + restaurantId));
    }
}