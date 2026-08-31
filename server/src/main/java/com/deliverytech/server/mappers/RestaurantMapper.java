package com.deliverytech.server.mappers;

import org.springframework.stereotype.Component;

import com.deliverytech.server.dtos.requests.RestaurantRequestDto;
import com.deliverytech.server.dtos.responses.RestaurantResponseDto;
import com.deliverytech.server.models.entity.Restaurant;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(RestaurantRequestDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.name());
        restaurant.setCategory(dto.category());
        restaurant.setActive(dto.active());
        restaurant.setRating(dto.rating());
        restaurant.setAddress(dto.address());
        restaurant.setPhone(dto.phone());
        restaurant.setDeliveryFee(dto.deliveryFee());
        restaurant.setCep(dto.cep());
        restaurant.setDeliveryTimeMinutes(dto.deliveryTimeMinutes());
        restaurant.setLatitude(dto.latitude());
        restaurant.setLongitude(dto.longitude());
        return restaurant;
    }

    public RestaurantResponseDto toResponse(Restaurant restaurant) {
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getActive(),
                restaurant.getRating(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getDeliveryFee(),
                restaurant.getCep(),
                restaurant.getDeliveryTimeMinutes(),
                restaurant.getLatitude(),
                restaurant.getLongitude());
    }
}
