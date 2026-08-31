package com.deliverytech.server.exceptions;

/** Thrown when an operation violates a domain business rule. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
