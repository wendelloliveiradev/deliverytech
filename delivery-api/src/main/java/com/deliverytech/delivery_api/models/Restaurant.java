package com.deliverytech.delivery_api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String category;
    private Boolean active;
    private Double rating;
    private String address;
    private String phone;
    private BigDecimal deliveryFee;

    public Restaurant(String name, String category, Boolean active, Double rating) {
        this.name = name;
        this.category = category;
        this.active = active;
        this.rating = rating;
    }
}