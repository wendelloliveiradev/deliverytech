package com.deliverytech.server.exceptions;

/**
 * Thrown when request data fails semantic validation not covered by bean
 * validation.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
