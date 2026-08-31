package com.deliverytech.delivery_api.exceptions;

/** Thrown when a required entity cannot be found by its identifier. */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
