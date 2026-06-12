package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem() {
    }

    public OrderItem(Integer quantity,
            Double unitPrice,
            FoodOrder order,
            Product product) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.order = order;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public FoodOrder getOrder() {
        return order;
    }

    public void setOrder(FoodOrder order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}