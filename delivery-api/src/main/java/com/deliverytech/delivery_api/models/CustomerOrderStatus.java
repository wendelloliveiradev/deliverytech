package com.deliverytech.delivery_api.models;

public enum CustomerOrderStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmado"),
    PREPARING("Preparando"),
    SHIPPED("Saiu para Entrega"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado");

    private final String description;

    CustomerOrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
