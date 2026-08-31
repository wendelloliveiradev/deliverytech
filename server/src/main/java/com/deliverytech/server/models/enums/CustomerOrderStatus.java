package com.deliverytech.server.models.enums;

import com.deliverytech.server.exceptions.InvalidCustomerOrderStatusTransitionException;

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

    /**
     * Validates status transitions.
     */
    public void ensureTransitionTo(
            CustomerOrderStatus next) {

        if (next == null) {
            throw new InvalidCustomerOrderStatusTransitionException(
                    this, null);
        }

        /**
         * Validates that the status transition is valid.
         */
        if (!this.canTransitionTo(next)) {
            throw new InvalidCustomerOrderStatusTransitionException(
                    this, next);
        }
    }

    /**
     * Checks if a status can transition to another.
     */
    public boolean canTransitionTo(CustomerOrderStatus next) {
        return switch (this) {
            case PENDING ->
                next == CONFIRMED ||
                        next == CANCELLED;

            case CONFIRMED ->
                next == PREPARING ||
                        next == CANCELLED;

            case PREPARING ->
                next == SHIPPED ||
                        next == CANCELLED;

            case SHIPPED ->
                next == DELIVERED;

            case DELIVERED,
                    CANCELLED ->
                false;
        };
    }
}
