package com.deliverytech.server.config;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.stereotype.Component;

import com.deliverytech.server.models.enums.CustomerOrderStatus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMetrics {
    private final MeterRegistry meterRegistry;

    public void recordProcessed(CustomerOrderStatus status, BigDecimal totalAmount) {
        Counter.builder("delivery.orders.processed")
                .tag("status", status.name())
                .register(meterRegistry)
                .increment();
        Counter.builder("delivery.orders.revenue")
                .tag("currency", "BRL")
                .register(meterRegistry)
                .increment(totalAmount.doubleValue());
    }

    public void recordFailure(String operation) {
        Counter.builder("delivery.orders.failed")
                .tag("operation", operation)
                .register(meterRegistry)
                .increment();
    }

    public void recordProcessingDuration(Duration duration) {
        Timer.builder("delivery.orders.processing")
                .publishPercentileHistogram()
                .register(meterRegistry)
                .record(duration);
    }
}