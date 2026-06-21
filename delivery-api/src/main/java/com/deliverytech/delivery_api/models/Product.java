package com.deliverytech.delivery_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Double price;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Product(String name,
            String category,
            Double price,
            Boolean available,
            Restaurant restaurant) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.restaurant = restaurant;
    }
}
