package com.deliverytech.server.exceptions;

/**
 * Thrown when a transactional operation cannot be completed consistently (e.g.
 * concurrent update conflicts).
 */
public class TransactionException extends RuntimeException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
