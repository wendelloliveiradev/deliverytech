package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.requests.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.responses.RestaurantResponseDto;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDto register(RestaurantRequestDto restaurant);

    RestaurantResponseDto findById(Long id);

    List<RestaurantResponseDto> findByName(String name);

    List<RestaurantResponseDto> findByCategory(String category);

    List<RestaurantResponseDto> findAllActive();

    List<RestaurantResponseDto> findTopRated();

    RestaurantResponseDto update(Long id, RestaurantRequestDto updatedRestaurant);

    void activate(Long id);

    void deactivate(Long id);

    void calculateDeliveryFee(Long restaurantId, Double distance);
}