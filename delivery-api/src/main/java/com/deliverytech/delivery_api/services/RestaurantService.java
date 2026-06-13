package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.models.Restaurant;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Registers a new restaurant.
     */
    public Restaurant register(Restaurant restaurant) {

        validateRestaurantData(restaurant);

        if (restaurant.getActive() == null) {
            restaurant.setActive(true);
        }

        if (restaurant.getRating() == null) {
            restaurant.setRating(0.0);
        }

        return restaurantRepository.save(restaurant);
    }

    /**
     * Finds a restaurant by ID.
     */
    public Restaurant findById(Long id) {

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));
    }

    /**
     * Finds restaurants by name.
     */
    public List<Restaurant> findByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty.");
        }

        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Finds restaurants by category.
     */
    public List<Restaurant> findByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Category cannot be empty.");
        }

        return restaurantRepository.findByCategory(category);
    }

    /**
     * Returns all active restaurants.
     */
    public List<Restaurant> findAllActive() {
        return restaurantRepository.findByActive(true);
    }

    /**
     * Returns all restaurants ordered by rating.
     */
    public List<Restaurant> findTopRated() {
        return restaurantRepository.findAllByOrderByRatingDesc();
    }

    /**
     * Updates a restaurant.
     */
    public Restaurant update(Long id, Restaurant updatedRestaurant) {

        Restaurant existingRestaurant = findById(id);

        validateRestaurantData(updatedRestaurant);

        existingRestaurant.setName(updatedRestaurant.getName());
        existingRestaurant.setCategory(updatedRestaurant.getCategory());
        existingRestaurant.setRating(updatedRestaurant.getRating());

        return restaurantRepository.save(existingRestaurant);
    }

    /**
     * Inactivates a restaurant.
     */
    public void inactivate(Long id) {

        Restaurant restaurant = findById(id);

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new IllegalArgumentException(
                    "Restaurant is already inactive.");
        }

        restaurant.setActive(false);

        restaurantRepository.save(restaurant);
    }

    /**
     * Activates a restaurant.
     */
    public void activate(Long id) {

        Restaurant restaurant = findById(id);

        if (Boolean.TRUE.equals(restaurant.getActive())) {
            throw new IllegalArgumentException(
                    "Restaurant is already active.");
        }

        restaurant.setActive(true);

        restaurantRepository.save(restaurant);
    }

    /**
     * Validates restaurant business rules.
     */
    private void validateRestaurantData(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant cannot be null.");
        }

        if (restaurant.getName() == null ||
                restaurant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Restaurant name is required.");
        }

        if (restaurant.getCategory() == null ||
                restaurant.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Restaurant category is required.");
        }

        if (restaurant.getRating() != null) {

            if (restaurant.getRating() < 0 ||
                    restaurant.getRating() > 5) {

                throw new IllegalArgumentException(
                        "Rating must be between 0 and 5.");
            }
        }
    }
}