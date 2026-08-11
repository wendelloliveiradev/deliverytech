package com.deliverytech.delivery_api.mappers;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.dtos.RestaurantRequestDto;
import com.deliverytech.delivery_api.dtos.RestaurantResponseDto;
import com.deliverytech.delivery_api.models.entity.Restaurant;

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
                restaurant.getDeliveryFee());
    }
}
