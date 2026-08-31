package com.deliverytech.server.services.interfaces;

import com.deliverytech.server.dtos.requests.RestaurantRequestDto;
import com.deliverytech.server.dtos.responses.RestaurantResponseDto;

import java.math.BigDecimal;
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

    BigDecimal calculateDeliveryFee(Long restaurantId, String cep);

    List<RestaurantResponseDto> findNearby(String cep);
}