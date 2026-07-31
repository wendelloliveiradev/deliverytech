# Delivery Tech API

Spring Boot REST API for a delivery domain. The project models customers, restaurants, products, orders, and order items, uses an in-memory H2 database, and seeds sample data on startup.

## Current Status

- Customer CRUD is implemented through `/customers`.
- Domain services exist for customers, restaurants, products, and orders.
- The `/restaurants`, `/products`, and `/customers-orders` controllers are scaffolds and are not production-ready endpoints yet.

## Stack

- Spring Boot 4.0.6
- Java 25, as configured in `pom.xml`
- Spring WebMVC
- Spring Data JPA
- Spring Validation
- H2 Database
- Lombok
- Maven Wrapper

## Run Locally

1. Install JDK 25.
2. From the `delivery-api/` directory, run `./mvnw spring-boot:run`.
3. Open `http://localhost:8080/h2-console` if you want to inspect the database.

## Configuration

- Server port: `8080`
- Database: in-memory H2 at `jdbc:h2:mem:deliverydb`
- H2 console: `/h2-console`
- Schema mode: `create-drop`
- SQL logging: enabled for development
- Open Session in View: disabled

## Seed Data

On startup, `DataLoader` inserts:

- 3 customers
- 2 restaurants
- 5 products
- 2 customer orders

## Implemented API

### Customers

- `POST /customers`
- `GET /customers`
- `GET /customers/{id}`
- `PUT /customers/{id}`
- `DELETE /customers/{id}`

### Customer rules

- Email must be unique.
- New customers default to active when the flag is omitted.
- Listing customers returns only active records.
- Deletion is implemented as inactivation.

## Domain Rules

- Customers must be active to place orders.
- Product prices must be greater than zero.
- Restaurant ratings must stay between 0 and 5.
- Order status transitions are validated by `CustomerOrderStatus`.

## Order Status Flow

Valid transitions are:

- `PENDING` -> `CONFIRMED` or `CANCELLED`
- `CONFIRMED` -> `PREPARING` or `CANCELLED`
- `PREPARING` -> `SHIPPED` or `CANCELLED`
- `SHIPPED` -> `DELIVERED`

## Documentation

- See [docs/README.md](../docs/README.md) for the documentation index.
- See [docs/current-state.md](../docs/current-state.md) for the current implementation map and domain overview.

## Notes

- The project currently exposes model entities directly from the controller layer.
- There is no dedicated health endpoint in the current codebase.
- The default test suite only includes the Spring Boot context smoke test.
