---
description: "Use when creating or editing Spring Boot backend code in this repository. Covers controller-service-repository layering, Lombok usage, transactions, status handling, and test expectations."
applyTo: "delivery-api/src/main/java/com/deliverytech/delivery_api/**/*.java, delivery-api/src/test/java/**/*.java"
---
# Delivery API Java Conventions

- Keep the existing layered structure:
  - Controllers in controllers package handle HTTP concerns only.
  - Services in services package contain business rules and transaction boundaries.
  - Repositories in repositories package handle persistence access only.

- Prefer constructor injection with final fields and Lombok @RequiredArgsConstructor.
- Keep using Lombok for simple model/service boilerplate when existing classes already use it.

- In controllers:
  - Return ResponseEntity for endpoints that can vary by status code.
  - Do not place business logic in controller methods.
  - For all new endpoints, use DTOs for request/response contracts instead of exposing entities directly.

- In services:
  - Put business validation and state-transition rules here.
  - Use @Transactional for write operations or classes that coordinate writes.

- For order status logic:
  - Use StatusOrder enum as the source of truth.
  - Persist enum values as strings and avoid ad-hoc status literals.

- Repositories should extend JpaRepository and prefer derived query methods first.
- Use custom @Query only when derived methods are not sufficient.

- Preserve current package naming and project structure unless explicitly requested.

- For new features, add tests close to the changed layer:
  - Service logic: unit tests.
  - Repository custom query: repository/data tests.
  - HTTP behavior: controller/web tests.