package com.deliverytech.delivery_api.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.deliverytech.delivery_api.utils.StatusOrder;

@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private StatusOrder status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public FoodOrder() {
    }

    public FoodOrder(LocalDateTime orderDate,
            StatusOrder status,
            Client client) {
        this.orderDate = orderDate;
        this.status = status;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
