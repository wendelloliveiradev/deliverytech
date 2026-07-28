package com.deliverytech.delivery_api.exceptions;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
