package com.deliverytech.delivery_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Boolean active;

    private Double rating;

    public Restaurant(String name, String category, Boolean active, Double rating) {
        this.name = name;
        this.category = category;
        this.active = active;
        this.rating = rating;
    }
}