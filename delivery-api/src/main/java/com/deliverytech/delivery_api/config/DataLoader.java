package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.models.*;
import com.deliverytech.delivery_api.utils.StatusOrder;

import lombok.RequiredArgsConstructor;

import com.deliverytech.delivery_api.repositories.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    // private final ProductRepository productRepository;
    // private final CustomerOrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");
    }

    private void insertCustomers() {
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

        customerRepository.saveAll(Arrays.asList(client1, client2, client3));
        System.out.println(" 3 clientes inseridos");
    }

    private void insertRestaurants() {
        System.out.println("--- Inserindo Restaurantes ---");

        Restaurant restaurantel = new Restaurant();
        restaurantel.setName("Pizza Express");
        restaurantel.setCategory("Italiana");
        restaurantel.setAddress("Av. Principal, 100");
        restaurantel.setPhone("1133333333");
        restaurantel.setDeliveryFee(new BigDecimal("3.50"));
        restaurantel.setActive(true);

        Restaurant restaurante2 = new Restaurant();
        restaurante2.setName("Burger King");
        restaurante2.setCategory("Fast Food");
        restaurante2.setAddress("Rua Secundária, 200");
        restaurante2.setPhone("1144444444");
        restaurante2.setDeliveryFee(new BigDecimal("5.00"));
        restaurante2.setActive(true);

        restaurantRepository.saveAll(Arrays.asList(restaurantel, restaurante2));

        System.out.println(" 2 restaurantes inseridos");
    }

    private void testQueries() {
        System.out.println("\n== TESTANDO CONSULTAS DOS REPOSITORIES ==");

        // Teste ClienteRepository
        System.out.println("\nTestes ClienteRepository");
        var clienteByEmail = customerRepository.findByEmail("joao@email.com");

        System.out.println("Cliente por email: " +
                clienteByEmail.map(Customer::getName).orElse("Não encontrado"));
        var clientesAtivos = customerRepository.findByActive(true);

        System.out.println("Clientes ativos: " + clientesAtivos.size());
        var clientesPorNome = customerRepository.findByNameContainingIgnoreCase("silva");

        System.out.println("Clientes com 'silva' no nome: " +
                clientesPorNome.size());
        boolean existeEmail = customerRepository.existsByEmail("maria@email.com");
        System.out.println("Existe cliente com email: " + existeEmail);
    }
}
