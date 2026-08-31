package com.deliverytech.server.services.implementations;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.server.config.JwtService;
import com.deliverytech.server.dtos.requests.LoginRequestDto;
import com.deliverytech.server.dtos.requests.UserCreateRequestDto;
import com.deliverytech.server.dtos.responses.AuthResponseDto;
import com.deliverytech.server.dtos.responses.UserResponseDto;
import com.deliverytech.server.exceptions.BusinessException;
import com.deliverytech.server.exceptions.EntityNotFoundException;
import com.deliverytech.server.models.entity.User;
import com.deliverytech.server.models.entity.Restaurant;
import com.deliverytech.server.models.enums.UserRole;
import com.deliverytech.server.repositories.RestaurantRepository;
import com.deliverytech.server.repositories.UserRepository;
import com.deliverytech.server.services.interfaces.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
        try {
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
            if (!Boolean.TRUE.equals(user.getActive())
                    || !passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password.");
            }
            log.info("audit_event=authentication_succeeded userId={} role={}", user.getId(), user.getRole());
            return new AuthResponseDto(jwtService.generateToken(user), "Bearer", jwtService.getExpirationSeconds(),
                    toResponse(user));
        } catch (BadCredentialsException ex) {
            log.warn("audit_event=authentication_failed");
            throw ex;
        }
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