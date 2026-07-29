package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.models.entity.Customer;
import com.deliverytech.delivery_api.models.entity.CustomerOrder;
import com.deliverytech.delivery_api.models.entity.OrderItem;
import com.deliverytech.delivery_api.models.entity.Product;
import com.deliverytech.delivery_api.models.entity.Restaurant;
import com.deliverytech.delivery_api.models.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.repositories.CustomerOrderRepository;
import com.deliverytech.delivery_api.repositories.CustomerRepository;
import com.deliverytech.delivery_api.repositories.ProductRepository;
import com.deliverytech.delivery_api.repositories.RestaurantRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");
        List<Customer> customers = insertCustomers();
        List<Restaurant> restaurants = insertRestaurants();
        List<Product> products = insertProducts(restaurants);
        List<CustomerOrder> orders = insertOrders(customers, products);
        testQueries(customers, restaurants, products, orders);
        System.out.println("=== CARGA DE DADOS CONCLUÍDA ===");
    }

    /**
     * Insere clientes de teste no banco de dados.
     */
    private List<Customer> insertCustomers() {
        System.out.println("--- Inserindo clientes ---");

        Customer client1 = new Customer();
        client1.setName("João Silva");
        client1.setEmail("joao@email.com");
        client1.setPhone("11987654321");
        client1.setAddress("Rua A, 123, São Paulo");
        client1.setActive(true);

        Customer client2 = new Customer();
        client2.setName("Maria Santos");
        client2.setEmail("maria@email.com");
        client2.setPhone("11998765432");
        client2.setAddress("Avenida B, 456, Rio de Janeiro");
        client2.setActive(true);

        Customer client3 = new Customer();
        client3.setName("Pedro Oliveira");
        client3.setEmail("pedro@email.com");
        client3.setPhone("11912345678");
        client3.setAddress("Travessa C, 789, Belo Horizonte");
        client3.setActive(false);

        List<Customer> savedCustomers = customerRepository.saveAll(Arrays.asList(client1, client2, client3));
        System.out.println(" 3 clientes inseridos");
        return savedCustomers;
    }

    private List<Restaurant> insertRestaurants() {
        System.out.println("--- Inserindo Restaurantes ---");

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Pizza Express");
        restaurant1.setCategory("Italiana");
        restaurant1.setAddress("Av. Principal, 100");
        restaurant1.setPhone("1133333333");
        restaurant1.setDeliveryFee(new BigDecimal("4.90"));
        restaurant1.setActive(true);
        restaurant1.setRating(4.8);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Burger King");
        restaurant2.setCategory("Fast Food");
        restaurant2.setAddress("Rua Secundária, 200");
        restaurant2.setPhone("1144444444");
        restaurant2.setDeliveryFee(new BigDecimal("7.50"));
        restaurant2.setActive(true);
        restaurant2.setRating(4.6);

        List<Restaurant> savedRestaurants = restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2));

        System.out.println(" 2 restaurantes inseridos");
        return savedRestaurants;
    }

    private List<Product> insertProducts(List<Restaurant> restaurants) {
        System.out.println("--- Inserindo produtos ---");

        Product product1 = new Product("Pizza Margherita", "Pizza", new BigDecimal("32.90"), true,
                restaurants.get(0));
        Product product2 = new Product("Pizza Calabresa", "Pizza", new BigDecimal("36.90"), true,
                restaurants.get(0));
        Product product3 = new Product("Pasta Carbonara", "Pasta", new BigDecimal("42.00"), true,
                restaurants.get(0));
        Product product4 = new Product("Smash Burger", "Burger", new BigDecimal("29.90"), true,
                restaurants.get(1));
        Product product5 = new Product("BBQ Ribs", "Barbecue", new BigDecimal("54.90"), true,
                restaurants.get(1));

        List<Product> savedProducts = productRepository.saveAll(
                Arrays.asList(product1, product2, product3, product4, product5));
        System.out.println(" 5 produtos inseridos");
        return savedProducts;
    }

    private List<CustomerOrder> insertOrders(List<Customer> customers, List<Product> products) {
        System.out.println("--- Inserindo pedidos com itens ---");

        CustomerOrder order1 = buildOrder(customers.get(0),
                new OrderLine(products.get(0), 1),
                new OrderLine(products.get(2), 2));

        CustomerOrder order2 = buildOrder(customers.get(1),
                new OrderLine(products.get(3), 2),
                new OrderLine(products.get(4), 1));

        List<CustomerOrder> savedOrders = customerOrderRepository.saveAll(Arrays.asList(order1, order2));
        System.out.println(" 2 pedidos inseridos");
        return savedOrders;
    }

    private CustomerOrder buildOrder(Customer customer, OrderLine... lines) {
        List<OrderItem> items = new ArrayList<>();
        CustomerOrder order = new CustomerOrder(LocalDateTime.now(), CustomerOrderStatus.PENDING, customer,
                calculateTotal(lines), items);

        for (OrderLine line : lines) {
            OrderItem item = new OrderItem();
            item.setCustomerOrder(order);
            item.setProduct(line.product());
            item.setQuantity(line.quantity());
            item.setSubtotal(line.product().getPrice().multiply(BigDecimal.valueOf(line.quantity())));
            items.add(item);
        }

        return order;
    }

    private BigDecimal calculateTotal(OrderLine... lines) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderLine line : lines) {
            total = total.add(line.product().getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }

        return total;
    }

    private void testQueries(List<Customer> customers, List<Restaurant> restaurants, List<Product> products,
            List<CustomerOrder> orders) {
        System.out.println("\n== TESTANDO CONSULTAS DOS REPOSITORIES ==");
        System.out.println("Clientes carregados: "
                + customers.stream().map(customer -> customer.getName()).collect(Collectors.joining(", ")));
        System.out.println("Pedidos gerados: " + orders.size());

        var restaurantsByDeliveryFee = restaurantRepository.findByDeliveryFeeLessThanEqual(new BigDecimal("5.00"));
        System.out.println("Restaurantes com taxa de entrega <= 5.00: "
                + restaurantsByDeliveryFee.stream().map(restaurant -> restaurant.getName())
                        .collect(Collectors.joining(", ")));

        var topRestaurants = restaurantRepository.findTop5ByOrderByNameAsc();
        System.out.println("Top 5 restaurantes por nome: "
                + topRestaurants.stream().map(restaurant -> restaurant.getName()).collect(Collectors.joining(", ")));

        var productsByPrice = productRepository.findByPriceLessThanEqual(new BigDecimal("50.00"));
        System.out.println("Produtos com preço <= 50.00: "
                + productsByPrice.stream().map(product -> product.getName()).collect(Collectors.joining(", ")));

        var topOrders = customerOrderRepository.findTop10ByOrderByOrderDateDesc();
        System.out.println("Pedidos mais recentes (top 10): "
                + topOrders.stream().map(order -> order.getId() + " - " + order.getCustomer().getName())
                        .collect(Collectors.joining(", ")));

        System.out.println("\nPedidos com relacionamentos carregados:");
        customerOrderRepository.findAllWithDetails().forEach(order -> {
            String items = order.getOrderItems().stream()
                    .map(item -> item.getQuantity() + "x " + item.getProduct().getName() + " @ "
                            + item.getProduct().getRestaurant().getName())
                    .collect(Collectors.joining(" | "));

            System.out.println("Pedido " + order.getId() + " | cliente=" + order.getCustomer().getName()
                    + " | total=" + order.getTotalAmount() + " | itens=" + items);
        });

        System.out.println("\nPersistência confirmada:");
        System.out.println("Clientes persistidos: " + customerRepository.count());
        System.out.println("Restaurantes persistidos: " + restaurantRepository.count());
        System.out.println("Produtos persistidos: " + productRepository.count());
        System.out.println("Pedidos persistidos: " + customerOrderRepository.count());
        System.out.println("Existe cliente joao@email.com: " + customerRepository.existsByEmail("joao@email.com"));
    }

    private record OrderLine(Product product, int quantity) {
    }
}
