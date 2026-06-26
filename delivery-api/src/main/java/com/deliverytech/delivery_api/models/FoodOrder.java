package com.deliverytech.delivery_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.utils.StatusOrder;

@Entity
@Table(name = "food_orders")
@Getter
@NoArgsConstructor
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Setter
    private StatusOrder status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Setter
    private Client client;

    private BigDecimal totalValue;

    @PrePersist
    private void prePersist() {
        this.orderDate = LocalDateTime.now();
    }

    public FoodOrder(LocalDateTime orderDate,
            StatusOrder status,
            Client client) {
        this.orderDate = orderDate;
        this.status = status;
        this.client = client;
    }
}
