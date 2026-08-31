package com.deliverytech.server.services.interfaces;

import com.deliverytech.server.dtos.requests.LoginRequestDto;
import com.deliverytech.server.dtos.requests.UserCreateRequestDto;
import com.deliverytech.server.dtos.responses.AuthResponseDto;
import com.deliverytech.server.dtos.responses.UserResponseDto;

public interface AuthService {
    UserResponseDto register(UserCreateRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

    UserResponseDto currentUser(String email);
}