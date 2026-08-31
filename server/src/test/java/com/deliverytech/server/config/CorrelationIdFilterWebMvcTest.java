package com.deliverytech.server.config;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.server.controllers.ProductController;
import com.deliverytech.server.dtos.responses.ProductResponseDto;
import com.deliverytech.server.services.interfaces.ProductService;

@WebMvcTest(ProductController.class)
@Import(CorrelationIdFilter.class)
class CorrelationIdFilterWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void propagatesValidCorrelationIdToResponse() throws Exception {
        when(productService.findById(1L)).thenReturn(new ProductResponseDto(1L, "Pizza", "Pizza", null, true, 1L));

        mockMvc.perform(get("/products/1").header(CorrelationIdFilter.HEADER_NAME, "request-42"))
                .andExpect(status().isOk())
                .andExpect(header().string(CorrelationIdFilter.HEADER_NAME, "request-42"));
    }

    @Test
    void createsCorrelationIdWhenHeaderIsMissing() throws Exception {
        when(productService.findById(1L)).thenReturn(new ProductResponseDto(1L, "Pizza", "Pizza", null, true, 1L));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(header().exists(CorrelationIdFilter.HEADER_NAME));
    }
}