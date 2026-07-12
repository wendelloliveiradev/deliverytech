package com.deliverytech.delivery_api.models;

import com.deliverytech.delivery_api.utils.StatusOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

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
@Setter
@NoArgsConstructor
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private StatusOrder status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal totalAmount;

    @PrePersist
    private void prePersist() {
        this.orderDate = LocalDateTime.now();
    }

    public CustomerOrder(LocalDateTime orderDate,
            StatusOrder status,
            Customer customer,
            BigDecimal totalAmount) {
        this.orderDate = orderDate;
        this.status = status;
        this.customer = customer;
        this.totalAmount = totalAmount;
    }
}
