# API Reference

Base URL: `http://localhost:8080`. New consumers should use the canonical `/api` routes. Request and response bodies are JSON.

## Security

`POST /api/auth/register` and `POST /api/auth/login` are public. Catalog reads under `/api/restaurants` and `/api/products` are public. Other `/api` routes require `Authorization: Bearer <jwt>`.

| Area        | Canonical endpoints                                                                                                                                                                                                                                                                                        |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Auth        | `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`                                                                                                                                                                                                                                      |
| Customers   | `POST /api/customers`, `GET /api/customers`, `GET /api/customers/{id}`, `GET /api/customers/email?email=`, `PUT /api/customers/{id}`, `DELETE /api/customers/{id}`                                                                                                                                         |
| Restaurants | `POST /api/restaurants`, `GET /api/restaurants?name=`, `GET /api/restaurants/{id}`, `GET /api/restaurants/category/{category}`, `GET /api/restaurants/top-rated`, `PUT /api/restaurants/{id}`, `PATCH /api/restaurants/{id}/activate`, `DELETE /api/restaurants/{id}`                                      |
| Delivery    | `GET /api/restaurants/{id}/delivery-fee/{cep}`, `GET /api/restaurants/nearby/{cep}`                                                                                                                                                                                                                        |
| Products    | `POST /api/products`, `GET /api/products?category=`, `GET /api/products/{id}`, `GET /api/products/restaurant/{restaurantId}`, `GET /api/products/category/{category}`, `PUT /api/products/{id}`, `PATCH /api/products/{id}/available`, `PATCH /api/products/{id}/unavailable`, `DELETE /api/products/{id}` |
| Orders      | `POST /api/customer-order`, `GET /api/customer-order?customerId=`, `GET /api/customer-order?status=`, `GET /api/customer-order?start=&end=`, `GET /api/customer-order/{id}`, `PATCH /api/customer-order/{id}/status`, `DELETE /api/customer-order/{id}`                                                    |
| Reports     | `GET /api/reports/sales-by-restaurants`, `GET /api/reports/best-selling-products`, `GET /api/reports/active-customers`, `GET /api/reports/customer-orders-by-date?start=&end=&status=`                                                                                                                     |

`DELETE` on customers/restaurants/products performs a logical deactivation or unavailability change; it does not delete the database row. `DELETE` on an order cancels it through the valid status-transition rules.

## Errors

Failures use a consistent JSON object:

```json
{
  "timestamp": "2026-08-31T20:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/products",
  "details": { "price": "Price must be greater than 0" }
}
```

The API uses `400` for invalid inputs, `401` for invalid credentials, `404` for missing resources, `409` for concurrent transaction conflicts, `422` for business-rule violations, and `500` for unexpected errors.

## Interactive Documentation

Swagger UI is available at `http://localhost:8080/swagger-ui.html`. Use its **Authorize** action to provide a JWT returned by `/api/auth/login`.
