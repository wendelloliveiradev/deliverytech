package com.deliverytech.server.services.implementations;

import com.deliverytech.server.dtos.requests.RestaurantRequestDto;
import com.deliverytech.server.dtos.responses.RestaurantResponseDto;
import com.deliverytech.server.exceptions.BusinessException;
import com.deliverytech.server.exceptions.EntityNotFoundException;
import com.deliverytech.server.exceptions.ValidationException;
import com.deliverytech.server.mappers.RestaurantMapper;
import com.deliverytech.server.models.entity.Restaurant;
import com.deliverytech.server.repositories.RestaurantRepository;
import com.deliverytech.server.services.interfaces.RestaurantService;
import com.deliverytech.server.services.interfaces.CepLocationService;

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
    private final CepLocationService cepLocationService;

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
        existingRestaurant.setCep(updatedRestaurant.getCep());
        existingRestaurant.setDeliveryTimeMinutes(updatedRestaurant.getDeliveryTimeMinutes());
        existingRestaurant.setLatitude(updatedRestaurant.getLatitude());
        existingRestaurant.setLongitude(updatedRestaurant.getLongitude());

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
    public BigDecimal calculateDeliveryFee(Long restaurantId, String cep) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant not found with ID: " + restaurantId));
        return restaurant.getDeliveryFee()
                .add(BigDecimal.valueOf(distanceInKilometers(restaurant, cepLocationService.resolve(cep))));
    }

    @Override
    public List<RestaurantResponseDto> findNearby(String cep) {
        CepLocationService.CepLocation location = cepLocationService.resolve(cep);
        return restaurantRepository.findByActiveTrue().stream()
                .filter(restaurant -> restaurant.getLatitude() != null && restaurant.getLongitude() != null)
                .filter(restaurant -> distanceInKilometers(restaurant, location) <= 10)
                .map(restaurantMapper::toResponse)
                .toList();
    }

    private double distanceInKilometers(Restaurant restaurant, CepLocationService.CepLocation location) {
        double latitudeDelta = Math.toRadians(location.latitude() - restaurant.getLatitude());
        double longitudeDelta = Math.toRadians(location.longitude() - restaurant.getLongitude());
        double value = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2)
                + Math.cos(Math.toRadians(restaurant.getLatitude())) * Math.cos(Math.toRadians(location.latitude()))
                        * Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(value), Math.sqrt(1 - value));
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