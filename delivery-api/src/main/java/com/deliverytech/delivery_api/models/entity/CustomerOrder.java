package com.deliverytech.delivery_api.models.entity;

import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

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
    private CustomerOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Version
    private Long version; // Para controle otimista de concorrência

    @PrePersist
    private void prePersist() {
        this.orderDate = LocalDateTime.now();
    }

    public CustomerOrder(LocalDateTime orderDate,
            CustomerOrderStatus status,
            Customer customer,
            BigDecimal totalAmount,
            List<OrderItem> orderItems) {
        this.orderDate = orderDate;
        this.status = status;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }
}
