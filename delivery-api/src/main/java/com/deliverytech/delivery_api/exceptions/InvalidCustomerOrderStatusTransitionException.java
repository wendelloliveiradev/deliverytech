package com.deliverytech.delivery_api.exceptions;

import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;

public class InvalidCustomerOrderStatusTransitionException extends BusinessException {

    public InvalidCustomerOrderStatusTransitionException(
            CustomerOrderStatus from,
            CustomerOrderStatus to) {

        super("Cannot transition from " + from + " to " + to);
    }
}
