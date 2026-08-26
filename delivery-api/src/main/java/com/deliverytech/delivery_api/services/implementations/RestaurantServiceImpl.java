package com.deliverytech.delivery_api.services.implementations;

import com.deliverytech.delivery_api.dtos.requests.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.responses.RestaurantResponseDto;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.exceptions.ValidationException;
import com.deliverytech.delivery_api.mappers.RestaurantMapper;
import com.deliverytech.delivery_api.models.entity.Restaurant;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;
import com.deliverytech.delivery_api.services.interfaces.RestaurantService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    @Transactional
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
            throw new ValidationException("Restaurant name cannot be empty.");
        }

        return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("Category cannot be empty.");
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
    @Transactional
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
    @Transactional
    public void activate(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        if (Boolean.TRUE.equals(restaurant.getActive())) {
            throw new BusinessException("Restaurant is already active.");
        }

        restaurant.setActive(true);

        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + id));

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new BusinessException("Restaurant is already inactive.");
        }

        restaurant.setActive(false);

        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void calculateDeliveryFee(Long restaurantId, Double distance) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + restaurantId));

        if (distance == null || distance < 0) {
            throw new ValidationException("Distance must be a non-negative value.");
        }

        BigDecimal baseFee = BigDecimal.valueOf(5); // Base delivery fee
        BigDecimal feePerKm = BigDecimal.valueOf(1); // Fee per kilometer
        BigDecimal deliveryFee = baseFee.add(feePerKm.multiply(BigDecimal.valueOf(distance)));

        restaurant.setDeliveryFee(deliveryFee);

        restaurantRepository.save(restaurant);
    }

    private void validateRestaurantData(Restaurant restaurant) {
        if (restaurant == null) {
            throw new ValidationException("Restaurant cannot be null.");
        }

        if (restaurant.getRating() != null && (restaurant.getRating() < 0 || restaurant.getRating() > 5)) {
            throw new ValidationException("Rating must be between 0 and 5.");
        }
    }
}