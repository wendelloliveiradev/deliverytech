package com.deliverytech.server.exceptions;

import com.deliverytech.server.models.enums.CustomerOrderStatus;

public class InvalidCustomerOrderStatusTransitionException extends BusinessException {

    public InvalidCustomerOrderStatusTransitionException(
            CustomerOrderStatus from,
            CustomerOrderStatus to) {

        super("Cannot transition from " + from + " to " + to);
    }
}
