package com.deliverytech.delivery_api.services.implementations;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.config.JwtService;
import com.deliverytech.delivery_api.dtos.requests.LoginRequestDto;
import com.deliverytech.delivery_api.dtos.requests.UserCreateRequestDto;
import com.deliverytech.delivery_api.dtos.responses.AuthResponseDto;
import com.deliverytech.delivery_api.dtos.responses.UserResponseDto;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.models.entity.User;
import com.deliverytech.delivery_api.models.entity.Restaurant;
import com.deliverytech.delivery_api.models.enums.UserRole;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;
import com.deliverytech.delivery_api.repositories.UserRepository;
import com.deliverytech.delivery_api.services.interfaces.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserCreateRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email is already registered.");
        }

        Restaurant restaurant = resolveRestaurant(request);
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .address(request.address())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() == null ? UserRole.CUSTOMER : request.role())
                .restaurant(restaurant)
                .active(true)
                .build();
        return toResponse(userRepository.save(user));
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
        if (!Boolean.TRUE.equals(user.getActive())
                || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        return new AuthResponseDto(jwtService.generateToken(user), "Bearer", jwtService.getExpirationSeconds(),
                toResponse(user));
    }

    @Override
    public UserResponseDto currentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        return toResponse(user);
    }

    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getAddress(),
                user.getRole());
    }

    private Restaurant resolveRestaurant(UserCreateRequestDto request) {
        UserRole role = request.role() == null ? UserRole.CUSTOMER : request.role();
        if (role != UserRole.RESTAURANT) {
            return null;
        }
        if (request.restaurantId() == null) {
            throw new BusinessException("Restaurant users must be associated with a restaurant.");
        }
        return restaurantRepository.findById(request.restaurantId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Restaurant not found with ID: " + request.restaurantId()));
    }
}