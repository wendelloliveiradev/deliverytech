package com.deliverytech.delivery_api.services.interfaces;

import com.deliverytech.delivery_api.dtos.requests.LoginRequestDto;
import com.deliverytech.delivery_api.dtos.requests.UserCreateRequestDto;
import com.deliverytech.delivery_api.dtos.responses.AuthResponseDto;
import com.deliverytech.delivery_api.dtos.responses.UserResponseDto;

public interface AuthService {
    UserResponseDto register(UserCreateRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

    UserResponseDto currentUser(String email);
}