package com.deliverytech.delivery_api.services.implementations;

import com.deliverytech.delivery_api.dtos.CustomerOrderCreateRequestDto;
import com.deliverytech.delivery_api.dtos.CustomerOrderResponseDto;
import com.deliverytech.delivery_api.dtos.OrderItemRequestDto;
import com.deliverytech.delivery_api.mappers.CustomerOrderMapper;
import com.deliverytech.delivery_api.models.entity.Customer;
import com.deliverytech.delivery_api.models.entity.CustomerOrder;
import com.deliverytech.delivery_api.models.entity.OrderItem;
import com.deliverytech.delivery_api.models.entity.Product;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.repositories.CustomerRepository;
import com.deliverytech.delivery_api.repositories.CustomerOrderRepository;
import com.deliverytech.delivery_api.repositories.ProductRepository;
import com.deliverytech.delivery_api.services.interfaces.CustomerOrderService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderMapper customerOrderMapper;

    @Override
    @Transactional
    public CustomerOrderResponseDto create(CustomerOrderCreateRequestDto orderRequestDto) {
        validateOrder(orderRequestDto);

        Customer customer = validateCustomer(orderRequestDto.customerId());
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderItems(buildOrderItems(orderRequestDto.orderItems(), order));
        order.setTotalAmount(calculateOrderTotal(order.getOrderItems()));

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() == null) {
            order.setStatus(CustomerOrderStatus.CONFIRMED);
        }

        CustomerOrder savedOrder = customerOrderRepository.save(order);
        return customerOrderMapper.toResponse(savedOrder);
    }

    @Override
    public CustomerOrderResponseDto findById(Long id) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with ID: " + id));

        return customerOrderMapper.toResponse(order);
    }

    @Override
    public List<CustomerOrderResponseDto> findByCustomer(Long customerId) {
        validateCustomer(customerId);
        return customerOrderRepository.findByCustomerId(customerId).stream()
                .map(customerOrderMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerOrderResponseDto> findByStatus(CustomerOrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        return customerOrderRepository.findByStatus(status).stream()
                .map(customerOrderMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerOrderResponseDto> findByPeriod(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        return customerOrderRepository.findByOrderDateBetweenDesc(start, end).stream()
                .map(customerOrderMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerOrderResponseDto changeStatus(Long orderId, CustomerOrderStatus newStatus) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with ID: " + orderId));

        order.getStatus().ensureTransitionTo(newStatus);
        order.setStatus(newStatus);

        CustomerOrder savedOrder = customerOrderRepository.save(order);
        return customerOrderMapper.toResponse(savedOrder);
    }

    @Override
    public CustomerOrderResponseDto cancel(Long orderId) {
        return changeStatus(orderId, CustomerOrderStatus.CANCELLED);
    }

    private void validateOrder(CustomerOrderCreateRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        if (orderRequestDto.customerId() == null) {
            throw new IllegalArgumentException("Customer ID is required.");
        }

        if (orderRequestDto.orderItems() == null || orderRequestDto.orderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item.");
        }
    }

    private Customer validateCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + customerId));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalArgumentException("Inactive customers cannot place orders.");
        }

        return customer;
    }

    private List<OrderItem> buildOrderItems(List<OrderItemRequestDto> orderItemDtos, CustomerOrder order) {
        return orderItemDtos.stream().map(orderItemDto -> {
            Product product = validateProduct(orderItemDto.productId());

            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.quantity());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(orderItemDto.quantity())));

            return orderItem;
        }).toList();
    }

    private BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product validateProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + productId));

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalArgumentException("Unavailable products cannot be added to an order.");
        }

        return product;
    }
}