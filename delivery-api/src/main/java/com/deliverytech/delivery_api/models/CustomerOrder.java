package com.deliverytech.delivery_api.models;

import com.deliverytech.delivery_api.utils.StatusOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers_orders")
@Getter
@NoArgsConstructor
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Setter
    private StatusOrder status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Setter
    private Customer customer;

    private BigDecimal totalValue;

    @PrePersist
    private void prePersist() {
        this.orderDate = LocalDateTime.now();
    }

    public CustomerOrder(LocalDateTime orderDate,
            StatusOrder status,
            Customer customer) {
        this.orderDate = orderDate;
        this.status = status;
        this.customer = customer;
    }
}
