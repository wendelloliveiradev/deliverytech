package com.deliverytech.delivery_api.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.dtos.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.dtos.OrderItemResponseDto;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.services.interfaces.CustomerOrderService;

@WebMvcTest(CustomerOrderController.class)
class CustomerOrderControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerOrderService customerOrderService;

    @Test
    void createCustomerOrder_whenInvalidPayload_shouldReturnBadRequest() throws Exception {
        String payload = """
                {
                  "customerId": null,
                  "orderItems": []
                }
                """;

        mockMvc.perform(post("/customers-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerOrderById_shouldReturnOrderResponseShape() throws Exception {
        CustomerOrderResponseDto responseDto = new CustomerOrderResponseDto(
                3L,
                LocalDateTime.of(2026, 1, 10, 12, 30),
                CustomerOrderStatus.CONFIRMED,
                1L,
                new BigDecimal("79.80"),
                List.of(new OrderItemResponseDto(11L, 8L, "Pizza", 2, new BigDecimal("79.80"))),
                0L);

        when(customerOrderService.findById(3L)).thenReturn(responseDto);

        mockMvc.perform(get("/customers-orders/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(79.80))
                .andExpect(jsonPath("$.orderItems[0].id").value(11))
                .andExpect(jsonPath("$.orderItems[0].productId").value(8))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Pizza"))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(2))
                .andExpect(jsonPath("$.orderItems[0].subtotal").value(79.80));

        verify(customerOrderService).findById(3L);
    }
}
