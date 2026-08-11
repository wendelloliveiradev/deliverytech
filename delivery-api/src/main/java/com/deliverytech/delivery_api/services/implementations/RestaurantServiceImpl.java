package com.deliverytech.delivery_api.services.implementations;

import com.deliverytech.delivery_api.dtos.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.RestaurantResponseDto;
import com.deliverytech.delivery_api.mappers.RestaurantMapper;
import com.deliverytech.delivery_api.models.entity.Restaurant;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;
import com.deliverytech.delivery_api.services.interfaces.RestaurantService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantResponseDto register(RestaurantRequestDto restaurantDto) {
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDto);
        validateRestaurantData(restaurant);

        if (restaurant.getActive() == null) {
            restaurant.setActive(true);
        }

        if (restaurant.getRating() == null) {
            restaurant.setRating(0.0);
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    public RestaurantResponseDto findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    public List<RestaurantResponseDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty.");
        }

        return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        return restaurantRepository.findByCategory(category).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findAllActive() {
        return restaurantRepository.findByActiveTrue().stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findTopRated() {
        return restaurantRepository.findAllByOrderByRatingDesc().stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public RestaurantResponseDto update(Long id, RestaurantRequestDto updatedRestaurantDto) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        Restaurant updatedRestaurant = restaurantMapper.toEntity(updatedRestaurantDto);

        validateRestaurantData(updatedRestaurant);

        existingRestaurant.setName(updatedRestaurant.getName());
        existingRestaurant.setCategory(updatedRestaurant.getCategory());
        existingRestaurant.setActive(updatedRestaurant.getActive());
        existingRestaurant.setRating(updatedRestaurant.getRating());
        existingRestaurant.setAddress(updatedRestaurant.getAddress());
        existingRestaurant.setPhone(updatedRestaurant.getPhone());
        existingRestaurant.setDeliveryFee(updatedRestaurant.getDeliveryFee());

        Restaurant savedRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    public void inactivate(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new IllegalArgumentException("Restaurant is already inactive.");
        }

        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void activate(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        if (Boolean.TRUE.equals(restaurant.getActive())) {
            throw new IllegalArgumentException("Restaurant is already active.");
        }

        restaurant.setActive(true);
        restaurantRepository.save(restaurant);
    }

    private void validateRestaurantData(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null.");
        }

        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required.");
        }

        if (restaurant.getCategory() == null || restaurant.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant category is required.");
        }

        if (restaurant.getRating() != null && (restaurant.getRating() < 0 || restaurant.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
    }
}