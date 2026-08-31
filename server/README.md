# DeliveryTech Server

Spring Boot REST API for the DeliveryTech application. It provides restaurant and product catalog management, customer orders, sales reporting, CEP-based delivery estimates, and JWT authentication.

## Stack

- Java 25 and Spring Boot 4.0.6
- Spring MVC, Spring Data JPA, Bean Validation, Spring Security
- Spring Boot Actuator, Micrometer, Prometheus registry, and Caffeine cache
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
- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`

`/actuator/health` is public. Metrics, Prometheus, and the remaining exposed Actuator endpoints require authentication. Requests accept or generate `X-Correlation-Id` and return it in the response.

The default H2 URL is `jdbc:h2:mem:deliverydb`. The schema uses `create-drop`; data is reset when the process stops. `DataLoader` creates three customers, two restaurants, five in-stock products, and two sample orders at startup.

## Configuration

| Property                 | Default                | Purpose                                                                    |
| ------------------------ | ---------------------- | -------------------------------------------------------------------------- |
| `JWT_SECRET`             | development-only value | HMAC key used to sign JWTs. Set a strong secret outside local development. |
| `JWT_EXPIRATION_MINUTES` | `60`                   | Bearer token lifetime.                                                     |
| `CEP_GEOCODER_BASE_URL`  | BrasilAPI CEP v2       | CEP geocoding service used for fee and nearby queries.                     |
| `APP_VERSION`            | `0.0.1-SNAPSHOT`       | Version reported by Actuator info.                                         |
| `GIT_COMMIT`             | `unknown`              | Revision reported by Actuator info.                                        |
| `app.cache.ttl`          | `10m`                  | Product cache expiry period.                                               |

## Build and Test

```bash
./mvnw clean verify
```

This runs the test suite and creates coverage output in `target/site/jacoco/index.html`.

## Container and CI

Build and run the local H2-backed container with:

```bash
docker compose up --build
```

The multi-stage `Dockerfile` runs Maven verification during its build and produces a non-root Java 25 runtime image. The GitHub Actions workflow runs Maven verification and confirms this image builds for pull requests and pushes to `main`.

There is no staging or production deployment target in this repository, so CI does not publish or deploy an image. A deployment environment, registry credentials, and a protected repository environment are external prerequisites before adding a deployment job.

## API Conventions

Canonical endpoints begin with `/api`. The older non-`/api` paths remain as compatibility aliases. Public endpoints include authentication and read-only restaurant/product catalog requests. Other canonical `/api` requests require `Authorization: Bearer <token>`.

Read the workspace [API Reference](../docs/API.md), [Architecture](../docs/ARCHITECTURE.md), and [Development Guide](../docs/DEVELOPMENT.md) for endpoint details and operational constraints.
