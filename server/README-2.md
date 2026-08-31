# DeliveryTech Server

Spring Boot REST API for the DeliveryTech application. It provides restaurant and product catalog management, customer orders, sales reporting, CEP-based delivery estimates, and JWT authentication.

## Stack

- Java 25 and Spring Boot 4.0.6
- Spring MVC, Spring Data JPA, Bean Validation, Spring Security
- H2 in-memory database for local development
- JJWT for stateless bearer-token authentication
- Springdoc OpenAPI 3.1 / Swagger UI
- JaCoCo coverage reporting

## Run Locally

```bash
./mvnw spring-boot:run
```

Useful local URLs:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console`

The default H2 URL is `jdbc:h2:mem:deliverydb`. The schema uses `create-drop`; data is reset when the process stops. `DataLoader` creates three customers, two restaurants, five in-stock products, and two sample orders at startup.

## Configuration

| Property                 | Default                | Purpose                                                                    |
| ------------------------ | ---------------------- | -------------------------------------------------------------------------- |
| `JWT_SECRET`             | development-only value | HMAC key used to sign JWTs. Set a strong secret outside local development. |
| `JWT_EXPIRATION_MINUTES` | `60`                   | Bearer token lifetime.                                                     |
| `CEP_GEOCODER_BASE_URL`  | BrasilAPI CEP v2       | CEP geocoding service used for fee and nearby queries.                     |

## Build and Test

```bash
./mvnw clean verify
```

This runs the test suite and creates coverage output in `target/site/jacoco/index.html`.

## API Conventions

Canonical endpoints begin with `/api`. The older non-`/api` paths remain as compatibility aliases. Public endpoints include authentication and read-only restaurant/product catalog requests. Other canonical `/api` requests require `Authorization: Bearer <token>`.

Read the workspace [API Reference](../docs/API.md), [Architecture](../docs/ARCHITECTURE.md), and [Development Guide](../docs/DEVELOPMENT.md) for endpoint details and operational constraints.
