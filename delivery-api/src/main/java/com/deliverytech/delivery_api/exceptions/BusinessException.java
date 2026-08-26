package com.deliverytech.delivery_api.exceptions;

/** Thrown when an operation violates a domain business rule. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
