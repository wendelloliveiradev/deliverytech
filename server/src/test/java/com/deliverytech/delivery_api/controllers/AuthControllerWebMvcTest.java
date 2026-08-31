package com.deliverytech.delivery_api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.dtos.responses.AuthResponseDto;
import com.deliverytech.delivery_api.dtos.responses.UserResponseDto;
import com.deliverytech.delivery_api.models.enums.UserRole;
import com.deliverytech.delivery_api.services.interfaces.AuthService;

@WebMvcTest(AuthController.class)
class AuthControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_whenValidPayload_shouldReturnCreatedWithoutPassword() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "Ana", "ana@example.com", "Rua A", UserRole.CUSTOMER);
        when(authService.register(any())).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"Ana","email":"ana@example.com","password":"secret123","address":"Rua A"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void login_whenInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        when(authService.login(any())).thenThrow(new BadCredentialsException("Invalid email or password."));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"ana@example.com\",\"password\":\"wrong-pass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }

    @Test
    void login_whenValidCredentials_shouldReturnBearerToken() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "Ana", "ana@example.com", "Rua A", UserRole.CUSTOMER);
        when(authService.login(any())).thenReturn(new AuthResponseDto("token-value", "Bearer", 3600, user));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"ana@example.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-value"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}