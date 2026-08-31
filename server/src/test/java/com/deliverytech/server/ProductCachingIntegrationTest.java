package com.deliverytech.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import com.deliverytech.server.services.interfaces.ProductService;

@SpringBootTest
class ProductCachingIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @Test
    void findByIdCachesProductAndInvalidatesItAfterAvailabilityChange() {
        productService.findById(1L);
        assertThat(cacheManager.getCache("products").get(1L)).isNotNull();

        productService.makeUnavailable(1L);

        assertThat(cacheManager.getCache("products").get(1L)).isNull();
        productService.findById(1L);
        assertThat(cacheManager.getCache("products").get(1L)).isNotNull();
    }
}