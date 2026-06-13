package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Product;
import com.deliverytech.delivery_api.models.Restaurant;
import com.deliverytech.delivery_api.repositories.ProductRepository;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductService(
            ProductRepository productRepository,
            RestaurantRepository restaurantRepository) {

        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Registers a new product.
     */
    public Product register(Product product) {

        validateProductData(product);

        Restaurant restaurant = validateRestaurant(
                product.getRestaurant().getId());

        product.setRestaurant(restaurant);

        if (product.getAvailable() == null) {
            product.setAvailable(true);
        }

        return productRepository.save(product);
    }

    /**
     * Finds a product by ID.
     */
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + id));
    }

    /**
     * Finds products belonging to a restaurant.
     */
    public List<Product> findByRestaurant(Long restaurantId) {

        validateRestaurant(restaurantId);

        return productRepository.findByRestaurantId(restaurantId);
    }

    /**
     * Finds products by category.
     */
    public List<Product> findByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Category is required.");
        }

        return productRepository.findByCategory(category);
    }

    /**
     * Finds all available products.
     */
    public List<Product> findAvailableProducts() {
        return productRepository.findByAvailable(true);
    }

    /**
     * Updates a product.
     */
    public Product update(Long id, Product updatedProduct) {

        Product existingProduct = findById(id);

        validateProductData(updatedProduct);

        Restaurant restaurant = validateRestaurant(
                updatedProduct.getRestaurant().getId());

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setRestaurant(restaurant);

        return productRepository.save(existingProduct);
    }

    /**
     * Makes a product unavailable.
     */
    public void makeUnavailable(Long id) {

        Product product = findById(id);

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalArgumentException(
                    "Product is already unavailable.");
        }

        product.setAvailable(false);

        productRepository.save(product);
    }

    /**
     * Makes a product available.
     */
    public void makeAvailable(Long id) {

        Product product = findById(id);

        if (Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalArgumentException(
                    "Product is already available.");
        }

        product.setAvailable(true);

        productRepository.save(product);
    }

    /**
     * Validates product business rules.
     */
    private void validateProductData(Product product) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null.");
        }

        if (product.getName() == null ||
                product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product name is required.");
        }

        if (product.getCategory() == null ||
                product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product category is required.");
        }

        if (product.getPrice() == null) {
            throw new IllegalArgumentException(
                    "Product price is required.");
        }

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Product price must be greater than zero.");
        }

        if (product.getRestaurant() == null) {
            throw new IllegalArgumentException(
                    "Restaurant is required.");
        }

        if (product.getRestaurant().getId() == null) {
            throw new IllegalArgumentException(
                    "Restaurant ID is required.");
        }
    }

    /**
     * Validates that the restaurant exists.
     */
    private Restaurant validateRestaurant(Long restaurantId) {

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: "
                                + restaurantId));
    }
}