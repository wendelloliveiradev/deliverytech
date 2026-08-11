package com.deliverytech.delivery_api.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.dtos.CustomerResponseDto;
import com.deliverytech.delivery_api.services.interfaces.CustomerService;

@WebMvcTest(CustomerController.class)
class CustomerControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void registerCustomer_whenInvalidPayload_shouldReturnBadRequest() throws Exception {
        String payload = """
                {
                  "name": "",
                  "email": "invalid-email",
                  "phone": "",
                  "address": "",
                  "active": true
                }
                """;

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerById_shouldReturnCustomerResponseShape() throws Exception {
        CustomerResponseDto responseDto = new CustomerResponseDto(
                1L,
                "John Doe",
                "john@example.com",
                "11999999999",
                "Street 10",
                true);

        when(customerService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.address").value("Street 10"))
                .andExpect(jsonPath("$.active").value(true));

        verify(customerService).findById(1L);
    }
}
