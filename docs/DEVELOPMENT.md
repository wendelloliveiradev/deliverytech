# Development Guide

## Prerequisites

- JDK 25
- Internet access for Maven dependencies and CEP geocoding when exercising CEP endpoints

## Commands

Run the API from `server/`:

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw clean verify
```

`clean verify` runs tests and writes the JaCoCo report to `target/site/jacoco/index.html`.

## Local Configuration

The local profile uses H2 and `create-drop`, with SQL logging enabled. Override JWT and geocoding settings with environment variables:

```bash
export JWT_SECRET='replace-with-a-strong-secret-at-least-32-bytes-long'
export JWT_EXPIRATION_MINUTES=60
export CEP_GEOCODER_BASE_URL='https://brasilapi.com.br/api/cep/v2'
```

Never deploy the default JWT secret. Use a durable production database and a configured secret manager before deploying this service.

## Client Workspace

`client/` intentionally contains no framework setup yet. Keep backend API clients, UI code, and frontend tooling there once the frontend stack is selected; do not place frontend build artifacts in `server/`.
