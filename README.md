# Transaction Aggregation API

A Spring Boot REST API for managing, categorizing, and aggregating financial transactions from multiple banking sources.

## Overview

This application provides endpoints to:
- Fetch transactions from multiple mock banking sources
- Categorize transactions automatically
- Aggregate transaction data by customer and category
- Generate summary reports with total amounts

## Technologies Used

- **Java 21** (with preview features enabled)
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven** (build tool)
- **Lombok** (code generation)
- **SpringDoc OpenAPI** (API documentation)

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## Getting Started

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

## API Documentation

Once the application is running, access the interactive API documentation:

**Swagger UI:** http://localhost:8080/swagger-ui.html

## Available Endpoints

### Transaction Endpoints

- **GET** `/api/transactions` - Retrieve all transactions
- **GET** `/api/transactions/{id}` - Get a specific transaction by ID
- **POST** `/api/transactions` - Create a new transaction
- **PUT** `/api/transactions/{id}` - Update an existing transaction
- **DELETE** `/api/transactions/{id}` - Delete a transaction

### Categorization Endpoints

- **POST** `/api/categorization/categorize` - Categorize transactions automatically
- **GET** `/api/categorization/categories` - Get all available transaction categories

### Aggregation Endpoints

- **GET** `/api/aggregation/summary` - Get aggregated summary of all transactions
- **GET** `/api/aggregation/by-customer/{customerId}` - Get transactions aggregated by customer
- **GET** `/api/aggregation/by-category/{category}` - Get transactions aggregated by category

## Database Access

The application uses an H2 in-memory database. You can access the H2 Console at:

**H2 Console:** http://localhost:8080/h2-console

**Connection Settings:**
- **JDBC URL:** `jdbc:h2:mem:transactions-db`
- **Username:** `SA`
- **Password:** (leave empty)

## Project Structure

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── TransactionAggregationApi.java    # Main application class
│   │   ├── controller/                        # REST controllers
│   │   │   ├── AggregationController.java
│   │   │   ├── CategorizationController.java
│   │   │   └── TransactionController.java
│   │   ├── datasource/                        # Mock data sources
│   │   │   ├── MockBankSourceA.java
│   │   │   └── MockBankSourceB.java
│   │   ├── dto/                               # Data Transfer Objects
│   │   │   └── TransactionDto.java
│   │   ├── exception/                         # Custom exceptions
│   │   │   └── ResourceNotFoundException.java
│   │   ├── mapper/                            # Entity-DTO mappers
│   │   │   └── TransactionMapper.java
│   │   ├── model/                             # Domain models
│   │   │   ├── AggregatedSummary.java
│   │   │   ├── Transaction.java
│   │   │   └── TransactionCategory.java
│   │   ├── repository/                        # Data repositories
│   │   │   └── TransactionRepository.java
│   │   ├── service/                           # Business logic
│   │   │   ├── AggregationService.java
│   │   │   ├── CategorizationService.java
│   │   │   ├── TransactionService.java
│   │   │   └── impl/                          # Service implementations
│   │   └── swagger/                           # Swagger configuration
│   │       └── swaggerConfig.java
│   └── resources/
│       └── application.properties             # Application configuration
└── test/                                       # Unit tests
    └── java/org/example/
        ├── controller/
        ├── repository/
        └── service/
```

## Transaction Categories

The API supports the following transaction categories:

- `FOOD`
- `TRAVEL`
- `SHOPPING`
- `UTILITIES`
- `GROCERIES`
- `ENTERTAINMENT`
- `OTHER`

## Example Usage

### Create a Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST001",
    "amount": 150.00,
    "description": "Grocery shopping",
    "date": "2026-01-25",
    "category": "GROCERIES"
  }'
```

### Get All Transactions

```bash
curl http://localhost:8080/api/transactions
```

### Get Aggregated Summary

```bash
curl http://localhost:8080/api/aggregation/summary
```

## Configuration

The application configuration can be found in `src/main/resources/application.properties`.

Default settings:
- Server port: `8080`
- Database: H2 in-memory
- JPA: Auto DDL creation enabled
- H2 Console: Enabled

## Running Tests

```bash
mvn test
```

## Building for Production

```bash
mvn clean package
java -jar target/transaction-aggregation-api-0.0.1-SNAPSHOT.jar
```

## License

This project is available for educational and demonstration purposes.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
