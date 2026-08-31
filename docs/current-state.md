# Current State

DeliveryTech is a working Spring Boot 4 delivery API with DTO-based REST controllers, H2 persistence, seed data, JWT authentication, OpenAPI documentation, and JaCoCo reporting.

## Implemented

- Customer, restaurant, product, and customer-order operations.
- Restaurant category, rating, CEP, coordinates, delivery fee, and delivery-time data.
- CEP-backed delivery-fee estimation and nearby restaurant lookup.
- Product description and stock tracking; successful orders reserve stock in the enclosing transaction.
- Order delivery address, status-transition validation, and cancellation flow.
- JWT registration, login, and current-user endpoints with BCrypt password hashes.
- Restaurant sales, best-selling product, active-customer, and date/status order reports.
- One centralized `ApiError` response for validation, not-found, business, transaction, malformed-request, invalid-credential, and unexpected failures.

## Compatibility

Canonical REST endpoints use `/api`. Legacy unprefixed controller mappings are still served for existing clients and tests. Only canonical `/api` mutating/reporting endpoints are currently protected by the HTTP security rule; see [API Reference](API.md) for the exact policy.

## Known Limitations

- CEP lookup depends on the configured external geocoder and returns a business error if coordinates are unavailable.
- The H2 database is intended for local development and resets at shutdown.
- Role claims are issued in JWTs, but resource-level ownership enforcement and role-specific controller rules have not yet been applied across the full API surface.
- The `client/` directory is a placeholder, not a frontend implementation.
