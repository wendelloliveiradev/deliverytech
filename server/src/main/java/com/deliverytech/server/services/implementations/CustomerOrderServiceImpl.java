package com.deliverytech.server.services.implementations;

import com.deliverytech.server.config.OrderMetrics;
import com.deliverytech.server.dtos.requests.CustomerOrderCreateRequestDto;
import com.deliverytech.server.dtos.requests.OrderItemRequestDto;
import com.deliverytech.server.dtos.responses.CustomerOrderResponseDto;
import com.deliverytech.server.exceptions.BusinessException;
import com.deliverytech.server.exceptions.EntityNotFoundException;
import com.deliverytech.server.exceptions.TransactionException;
import com.deliverytech.server.exceptions.ValidationException;
import com.deliverytech.server.mappers.CustomerOrderMapper;
import com.deliverytech.server.models.entity.Customer;
import com.deliverytech.server.models.entity.CustomerOrder;
import com.deliverytech.server.models.entity.OrderItem;
import com.deliverytech.server.models.entity.Product;
import com.deliverytech.server.models.entity.Restaurant;
import com.deliverytech.server.models.enums.CustomerOrderStatus;
import com.deliverytech.server.repositories.CustomerRepository;
import com.deliverytech.server.repositories.CustomerOrderRepository;
import com.deliverytech.server.repositories.ProductRepository;
import com.deliverytech.server.services.interfaces.CustomerOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderMapper customerOrderMapper;
    private final OrderMetrics orderMetrics;

    @Override
    @Transactional
    public CustomerOrderResponseDto create(CustomerOrderCreateRequestDto orderRequestDto) {
        long startNanos = System.nanoTime();
        try {
            validateOrder(orderRequestDto);

            Customer customer = validateCustomer(orderRequestDto.customerId());
            CustomerOrder order = new CustomerOrder();
            order.setCustomer(customer);
            order.setDeliveryAddress(orderRequestDto.deliveryAddress());

            List<OrderItem> orderItems = buildOrderItems(orderRequestDto.orderItems(), order);
            Restaurant restaurant = validateRestaurantOfItems(orderItems);
            order.setOrderItems(orderItems);
            order.setTotalAmount(calculateOrderTotal(orderItems, restaurant));

            if (order.getOrderDate() == null) {
                order.setOrderDate(LocalDateTime.now());
            }

            if (order.getStatus() == null) {
                order.setStatus(CustomerOrderStatus.CONFIRMED);
            }

            CustomerOrder savedOrder = saveOrder(order);
            orderMetrics.recordProcessed(savedOrder.getStatus(), savedOrder.getTotalAmount());
            log.info("audit_event=order_created orderId={} status={} totalAmount={}", savedOrder.getId(),
                    savedOrder.getStatus(), savedOrder.getTotalAmount());
            return customerOrderMapper.toResponse(savedOrder);
        } catch (RuntimeException ex) {
            orderMetrics.recordFailure("create");
            throw ex;
        } finally {
            orderMetrics.recordProcessingDuration(Duration.ofNanos(System.nanoTime() - startNanos));
        }
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
            throw new ValidationException("Status is required.");
        }

        return customerOrderRepository.findByStatus(status).stream()
                .map(customerOrderMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerOrderResponseDto> findByPeriod(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end dates are required.");
        }

        if (start.isAfter(end)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        return customerOrderRepository.findByOrderDateBetweenDesc(start, end).stream()
                .map(customerOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerOrderResponseDto changeStatus(Long orderId, CustomerOrderStatus newStatus) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with ID: " + orderId));

        order.getStatus().ensureTransitionTo(newStatus);
        order.setStatus(newStatus);

        CustomerOrder savedOrder = saveOrder(order);
        log.info("audit_event=order_status_changed orderId={} status={}", savedOrder.getId(), savedOrder.getStatus());
        return customerOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public CustomerOrderResponseDto cancel(Long orderId) {
        return changeStatus(orderId, CustomerOrderStatus.CANCELLED);
    }

    /**
     * Wraps optimistic-locking failures so concurrent updates surface as a
     * transactional error.
     */
    private CustomerOrder saveOrder(CustomerOrder order) {
        try {
            return customerOrderRepository.save(order);
        } catch (OptimisticLockingFailureException ex) {
            throw new TransactionException(
                    "Order was modified concurrently and could not be saved. Please retry.", ex);
        }
    }

    private void validateOrder(CustomerOrderCreateRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            throw new ValidationException("Order cannot be null.");
        }

        if (orderRequestDto.customerId() == null) {
            throw new ValidationException("Customer ID is required.");
        }

        if (orderRequestDto.orderItems() == null || orderRequestDto.orderItems().isEmpty()) {
            throw new ValidationException("Order must have at least one item.");
        }

        if (orderRequestDto.deliveryAddress() == null || orderRequestDto.deliveryAddress().isBlank()) {
            throw new ValidationException("Delivery address is required.");
        }
    }

    private Customer validateCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + customerId));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new BusinessException("Inactive customers cannot place orders.");
        }

        return customer;
    }

    private List<OrderItem> buildOrderItems(List<OrderItemRequestDto> orderItemDtos, CustomerOrder order) {
        return orderItemDtos.stream().map(orderItemDto -> {
            Product product = validateProduct(orderItemDto.productId());
            if (product.getStock() == null || product.getStock() < orderItemDto.quantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - orderItemDto.quantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.quantity());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(orderItemDto.quantity())));

            return orderItem;
        }).toList();
    }

    /**
     * Ensures every item belongs to the same restaurant and that restaurant exists
     * and is active.
     */
    private Restaurant validateRestaurantOfItems(List<OrderItem> orderItems) {
        Restaurant restaurant = orderItems.get(0).getProduct().getRestaurant();

        if (restaurant == null) {
            throw new EntityNotFoundException("Product is not associated with any restaurant.");
        }

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new BusinessException("Orders cannot be placed with an inactive restaurant.");
        }

        boolean allSameRestaurant = orderItems.stream()
                .allMatch(item -> restaurant.getId().equals(item.getProduct().getRestaurant().getId()));

        if (!allSameRestaurant) {
            throw new BusinessException("All products in an order must belong to the same restaurant.");
        }

        return restaurant;
    }

    private BigDecimal calculateOrderTotal(List<OrderItem> orderItems, Restaurant restaurant) {
        BigDecimal itemsTotal = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deliveryFee = restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO;

        return itemsTotal.add(deliveryFee);
    }

    private Product validateProduct(Long productId) {
        if (productId == null) {
            throw new ValidationException("Product ID is required.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + productId));

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessException("Unavailable products cannot be added to an order.");
        }

        return product;
    }
}