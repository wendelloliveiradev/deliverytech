# DeliveryTech

DeliveryTech is a delivery-management application workspace. The backend is a Spring Boot REST API for customer, restaurant, product, order, reporting, and authentication workflows. The frontend workspace is reserved for the forthcoming client application.

## Workspace

- `server/`: Spring Boot REST API.
- `client/`: frontend application placeholder.
- `docs/`: architecture, domain, API, and development documentation.

## Quick Start

```bash
cd server
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`. Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

Read [Architecture](docs/ARCHITECTURE.md), [Domain Model](docs/DOMAIN.md), [API Reference](docs/API.md), and [Development Guide](docs/DEVELOPMENT.md) for the application details.
