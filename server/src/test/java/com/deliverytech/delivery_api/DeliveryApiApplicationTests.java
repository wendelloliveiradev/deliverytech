package com.deliverytech.delivery_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.deliverytech.delivery_api.repositories.CustomerOrderRepository;
import com.deliverytech.delivery_api.repositories.CustomerRepository;
import com.deliverytech.delivery_api.repositories.ProductRepository;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;

@SpringBootTest
class DeliveryApiApplicationTests {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerOrderRepository customerOrderRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldPersistSeedDataAndSupportDerivedQueries() {
		assertThat(customerRepository.count()).isEqualTo(3);
		assertThat(restaurantRepository.count()).isEqualTo(2);
		assertThat(productRepository.count()).isEqualTo(5);
		assertThat(customerOrderRepository.count()).isEqualTo(2);

		var restaurantsByFee = restaurantRepository.findByDeliveryFeeLessThanEqual(new BigDecimal("5.00"));
		System.out.println("Restaurants by fee <= 5.00: "
				+ restaurantsByFee.stream().map(restaurant -> restaurant.getName()).toList());
		assertThat(restaurantsByFee).hasSize(1);

		var topRestaurants = restaurantRepository.findTop5ByOrderByNameAsc();
		System.out.println("Top restaurants by name: "
				+ topRestaurants.stream().map(restaurant -> restaurant.getName()).toList());
		assertThat(topRestaurants).hasSize(2);

		var productsByPrice = productRepository.findByPriceLessThanEqual(new BigDecimal("50.00"));
		System.out.println("Products by price <= 50.00: "
				+ productsByPrice.stream().map(product -> product.getName()).toList());
		assertThat(productsByPrice).hasSize(4);

		var recentOrders = customerOrderRepository.findTop10ByOrderByOrderDateDesc();
		System.out.println("Recent orders: "
				+ recentOrders.stream().map(order -> String.valueOf(order.getId())).toList());
		assertThat(recentOrders).hasSize(2);

		var detailedOrders = customerOrderRepository.findAllWithDetails();
		assertThat(detailedOrders).hasSize(2);
		assertThat(detailedOrders.getFirst().getCustomer().getName()).isNotBlank();
		assertThat(detailedOrders)
				.allSatisfy(order -> {
					assertThat(order.getCustomer()).isNotNull();
					assertThat(order.getOrderItems()).isNotEmpty();
					assertThat(order.getOrderItems().getFirst().getProduct().getRestaurant()).isNotNull();
				});
	}

}
