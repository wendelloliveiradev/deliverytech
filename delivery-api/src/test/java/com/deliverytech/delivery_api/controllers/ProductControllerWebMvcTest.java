package com.deliverytech.delivery_api.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.dtos.ProductResponseDto;
import com.deliverytech.delivery_api.services.interfaces.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void insertProduct_whenInvalidPayload_shouldReturnBadRequest() throws Exception {
        String payload = """
                {
                  "name": "",
                  "category": "",
                  "price": -1,
                  "available": true
                }
                """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductById_shouldReturnProductResponseShape() throws Exception {
        ProductResponseDto responseDto = new ProductResponseDto(
                8L,
                "Pizza",
                "Food",
                new BigDecimal("39.90"),
                true,
                2L);

        when(productService.findById(8L)).thenReturn(responseDto);

        mockMvc.perform(get("/products/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.price").value(39.90))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.restaurantId").value(2));

        verify(productService).findById(8L);
    }
}
