# Transaction Aggregation API

A Spring Boot REST API for managing, categorizing, and aggregating financial transactions from multiple banking sources.

## Overview

This application provides endpoints to:
- Manage CRUD operations for financial transactions
- Categorize transactions by type (Food, Travel, Shopping, etc.)
- Aggregate transaction data by customer and category
- Produce summary reports with totals per category

## Quick summary

- Framework: Spring Boot 3.2
- Project build: Maven
- Java: 17 or later (project is compiled with `--release 17`)
- Test tooling: JUnit 5, Mockito, MockMvc

> Note: Spring Boot 3 requires Java 17+. If your machine has a newer JDK (for example JDK 21), Maven should be run with that JDK set in `JAVA_HOME` or your IDE run configuration.

## Features

- Full CRUD endpoints for transactions
- Transaction categorization endpoint
- Aggregation endpoints (per-customer, summary totals)
- DTO-based API surface (clean separation between API layer and domain models)
- Comprehensive unit tests (controllers, services, models, repository)
- In-memory H2 database for local development
- OpenAPI / Swagger UI for interactive API documentation

## Technologies Used

- Java 17+ (project compiled with release 17)
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- Lombok
- SpringDoc OpenAPI
- JUnit 5, Mockito, MockMvc

## Prerequisites

- Java 17 or higher installed
- Maven 3.6+

Verify Java version:

```powershell
java -version
```

If Maven picks the wrong JDK, set `JAVA_HOME` for your session (PowerShell example):

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-17-or-later'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
mvn -v
```

## Build & Run

Build the project:

```powershell
mvn clean install
```

Run the application locally:

```powershell
mvn spring-boot:run
```

The service starts on http://localhost:8080 by default.

## API Documentation

When the app runs, the interactive Swagger UI is available at (SpringDoc):

```
http://localhost:8080/swagger-ui/index.html
```

(Older `swagger-ui.html` redirects are sometimes configured; use the `/swagger-ui/index.html` path for SpringDoc v2.)

## Endpoints (overview)

Transaction endpoints
- GET    /transactions                   — list all transactions
- GET    /transactions/{id}              — get transaction by id
- POST   /transactions                   — create transaction
- PUT    /transactions/{id}              — update transaction
- DELETE /transactions/{id}              — delete transaction

Categorization
- POST /transactions/{transactionId}/categorize — categorize a transaction (request body: category name string)

Aggregation
- GET /aggregation/summary                 — aggregated totals and category breakdown
- GET /aggregation/transactions/{customerId} — transactions for a specific customer

## DTOs (API models)

The API surface uses DTOs (in `org.example.dto`) to separate concerns:
- TransactionDto, TransactionCreateDto, TransactionUpdateDto, etc.
- AggregatedSummaryDto and other response DTOs

Controller tests in this project assert JSON against the DTO shapes (not internal domain entities).

## Database (H2)

The project uses an in-memory H2 database for development and tests.
H2 console (if enabled) is typically available at:

```
http://localhost:8080/h2-console
```

Default connection used in development (see `application.properties`):
- JDBC URL: `jdbc:h2:mem:transactions-db`
- Username: `SA`
- Password: (empty)

## Testing

This project includes unit tests for model, service, repository, and controller layers.

Important testing notes:
- Controller tests use `@WebMvcTest` and assert JSON responses using DTOs (e.g. `TransactionDto`).
- For controller tests the security filters are disabled during tests with `@AutoConfigureMockMvc(addFilters = false)`, and security helpers (e.g. `JwtUtil`) are mocked with `@MockBean` so the Spring test context can start without real authentication.
- ObjectMapper in tests registers Java Time modules (`objectMapper.findAndRegisterModules()`) to support `LocalDate` serialization/deserialization.

Run all tests:

```powershell
mvn test
```

Run a specific test class:

```powershell
mvn -Dtest=TransactionControllerTest test
mvn -Dtest=AggregationControllerTest test
mvn -Dtest=CategorizationControllerTest test
```

Run groups of tests (examples):

```powershell
# controller tests only
mvn -Dtest=*ControllerTest test

# service tests only
mvn -Dtest=*ServiceImplTest test
```

If Maven uses a different Java version than you expect, prefix the command with a `JAVA_HOME` environment override in PowerShell as shown earlier.

## Development notes

- Tests and controller assertions were recently migrated to validate API responses using DTOs. The tests still stub service layer behavior using domain entities (tests convert DTO → domain entity when stubbing).
- If you add new controller tests that interact with LocalDate, register Jackson JavaTime modules in the test setup.

## Example cURL requests

Create a transaction:

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "customer-123",
    "amount": 150.00,
    "description": "Grocery shopping",
    "date": "2026-01-25",
    "category": "GROCERIES"
  }'
```

Categorize a transaction (body is a JSON string with the category name):

```bash
curl -X POST http://localhost:8080/transactions/1/categorize \
  -H "Content-Type: application/json" \
  -d '"FOOD"'
```

Get aggregated summary:

```bash
curl http://localhost:8080/aggregation/summary
```

## Troubleshooting

- Build failures related to Java version: ensure `JAVA_HOME` points to a Java 17+ JDK (project compiles with `--release 17`).
- Controller tests failing to start Spring context with security beans: ensure controller tests disable filters and mock `JwtUtil` as in the project tests.
- Date parsing/serialization errors: register Java Time modules on the test ObjectMapper.

## Contribution & Next steps

- Consider adding pagination, filtering/search, and multi-currency support.
- Switch controller endpoints to accept/return DTOs explicitly (if not already) and centralize mapping in `TransactionMapper`.
- Add integration tests that exercise real security flows (authentication + authorization).

## License & Author

This project is provided for demonstration and educational purposes.

---

If you'd like, I can also: add a short CONTRIBUTING.md, add a Maven toolchains configuration to make selecting a JDK easier, or add a small README section showing how to run a single test method from PowerShell; tell me which you'd prefer.
