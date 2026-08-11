package com.deliverytech.delivery_api.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.dtos.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.dtos.OrderItemResponseDto;
import com.deliverytech.delivery_api.models.entity.CustomerOrder;
import com.deliverytech.delivery_api.models.entity.OrderItem;

@Component
public class CustomerOrderMapper {

    public CustomerOrderResponseDto toResponse(CustomerOrder order) {
        Long customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        return new CustomerOrderResponseDto(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                customerId,
                order.getTotalAmount(),
                toOrderItemResponses(order.getOrderItems()),
                order.getVersion());
    }

    private List<OrderItemResponseDto> toOrderItemResponses(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return List.of();
        }

        return orderItems.stream().map(this::toOrderItemResponse).toList();
    }

    private OrderItemResponseDto toOrderItemResponse(OrderItem orderItem) {
        Long productId = orderItem.getProduct() != null ? orderItem.getProduct().getId() : null;
        String productName = orderItem.getProduct() != null ? orderItem.getProduct().getName() : null;

        return new OrderItemResponseDto(
                orderItem.getId(),
                productId,
                productName,
                orderItem.getQuantity(),
                orderItem.getSubtotal());
    }
}
