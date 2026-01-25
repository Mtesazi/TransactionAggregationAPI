# Transaction Aggregation API

A Spring Boot REST API for managing, categorizing, and aggregating financial transactions from multiple banking sources.

## Overview

This application provides endpoints to:
- Manage complete CRUD operations for financial transactions
- Categorize transactions by type (Food, Travel, Shopping, etc.)
- Aggregate transaction data by customer and category
- Generate comprehensive summary reports with totals per category
- Track and analyze spending patterns

## Features

✅ **Full CRUD Operations** - Create, Read, Update, Delete transactions  
✅ **Transaction Categorization** - Automatic and manual categorization support  
✅ **Data Aggregation** - Summarize transactions by customer and category  
✅ **RESTful API** - Well-structured REST endpoints  
✅ **API Documentation** - Interactive Swagger UI documentation  
✅ **Comprehensive Testing** - 98 unit tests with complete coverage  
✅ **In-Memory Database** - H2 database for quick development and testing  

## Technologies Used

- **Java 21** (with preview features enabled)
- **Spring Boot 3.2.0**
- **Spring Data JPA** (data persistence)
- **H2 Database** (in-memory)
- **Maven** (build tool)
- **Lombok** (code generation)
- **SpringDoc OpenAPI 2.3.0** (API documentation)
- **JUnit 5** (testing framework)
- **Mockito** (mocking framework)
- **MockMvc** (Spring MVC testing)

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

- **GET** `/transactions` - Retrieve all transactions
- **GET** `/transactions/{id}` - Get a specific transaction by ID
- **POST** `/transactions` - Create a new transaction
- **PUT** `/transactions/{id}` - Update an existing transaction
- **DELETE** `/transactions/{id}` - Delete a transaction

### Categorization Endpoints

- **POST** `/transactions/{transactionId}/categorize` - Categorize a specific transaction

### Aggregation Endpoints

- **GET** `/aggregation/summary` - Get aggregated summary of all transactions with category totals
- **GET** `/aggregation/transactions/{customerId}` - Get all transactions for a specific customer

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
│   │   ├── dto/                               # Data Transfer Objects (8 DTOs)
│   │   │   ├── AggregatedSummaryDto.java
│   │   │   ├── ApiResponseDto.java
│   │   │   ├── CategorySummaryDto.java
│   │   │   ├── CustomerSummaryDto.java
│   │   │   ├── ErrorResponseDto.java
│   │   │   ├── TransactionCreateDto.java
│   │   │   ├── TransactionDto.java
│   │   │   ├── TransactionFilterDto.java
│   │   │   └── TransactionUpdateDto.java
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
└── test/                                       # Unit tests (98 tests)
    └── java/org/example/
        ├── controller/                        # Controller tests (17 tests)
        │   ├── AggregationControllerTest.java
        │   ├── CategorizationControllerTest.java
        │   └── TransactionControllerTest.java
        ├── model/                             # Model tests (53 tests)
        │   ├── AggregatedSummaryTest.java
        │   ├── TransactionCategoryTest.java
        │   └── TransactionTest.java
        ├── repository/                        # Repository tests (4 tests)
        │   └── TransactionRepositoryTest.java
        └── service/                           # Service tests (24 tests)
            ├── AggregationServiceImplTest.java
            ├── CategorizationServiceImplTest.java
            └── TransactionServiceImplTest.java
```

## Data Transfer Objects (DTOs)

The API uses DTOs for clean separation between API layer and domain layer:

### Request DTOs
- **TransactionCreateDto** - For creating new transactions (excludes auto-generated ID)
- **TransactionUpdateDto** - For updating existing transactions
- **TransactionFilterDto** - For filtering transactions by multiple criteria

### Response DTOs
- **TransactionDto** - Standard transaction response
- **AggregatedSummaryDto** - Aggregated summary with totals and category breakdowns
- **CategorySummaryDto** - Category-specific statistics
- **CustomerSummaryDto** - Customer-specific summaries with recent transactions
- **ErrorResponseDto** - Standardized error responses
- **ApiResponseDto<T>** - Generic wrapper for consistent API responses

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

**Response:**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "customerId": "customer-123",
  "amount": 150.00,
  "description": "Grocery shopping",
  "date": "2026-01-25",
  "category": "GROCERIES"
}
```

### Get All Transactions

```bash
curl http://localhost:8080/transactions
```

**Response:**
```json
[
  {
   Testing

The project includes comprehensive unit tests with 98 tests covering all layers:

### Test Coverage

- **Model Tests** (53 tests) - Transaction, AggregatedSummary, TransactionCategory
- **Service Tests** (24 tests) - TransactionService, AggregationService, CategorizationService
- **Controller Tests** (17 tests) - TransactionController, AggregationController, CategorizationController
- **Repository Tests** (4 tests) - TransactionRepository

### Run All Tests

```bash
mvn test
```

### Run Specific Test Classes

```bash
# Run controller tests only
mvn test -Dtest=*ControllerTest

# Run service tests only
mvn test -Dtest=*ServiceImplTest

# Run model tests only
mvn test -Dtest=TransactionTest,AggregatedSummaryTest,TransactionCategoryTest
```

### Test Results

All 98 tests pass successfully:
- ✅ **0 Failures**
- ✅ **0 Errors**
- ✅ **0 Skipped** "date": "2026-01-25",
    "category": "GROCERIES"
  },
  {
    "id": "2",
    "customerId": "customer-456",
    "amount": 75.50,
    "description": "Restaurant",
    "date": "2026-01-24",
    "category": "FOOD"
  }
]
```

### Get Transaction by ID

```bash
curl http://localhost:8080/transactions/1
```

### Update a Transaction

```bash
curl -X PUT http://localhost:8080/transactions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "customer-123",
    "amount": 175.00,
    "description": "Updated Grocery shopping",
    "date": "2026-01-25",
    "category": "GROCERIES"
  }'
```

### Delete a Transaction

```bash
curl -X DELETE http://localhost:8080/transactions/1
```

### Categorize a Transaction

```bash
curl -X POST http://localhost:8080/transactions/1/categorize \
  -H "Content-Type: application/json" \
  -d '"FOOD"'
```

### Get Aggregated Summary

```bash
curl http://localhost:8080/aggregation/summary
```

**Response:**
```json
{
  "total": 225.50,
### Create Production Build

```bash
mvn clean package
```

### Run Production Build

```bash
java -jar target/transaction-aggregation-api-0.0.1-SNAPSHOT.jar
```

### Build with Tests Skipped

```bash
mvn clean package -DskipTests
```

## Error Handling

The API includes proper error handling for common scenarios:

- **404 Not Found** - When a transaction ID doesn't exist
- **400 Bad Request** - When request data is invalid
- **500 Internal Server Error** - For unexpected server errors

## Development

### Code Quality

- Uses Lombok to reduce boilerplate code
- Follows Spring Boot best practices
- Implements proper separation of concerns (Controller → Service → Repository)
- Uses DTOs for data transfer
- Includes comprehensive exception handling

### Testing Strategy

- **Unit Tests** - Test individual components in isolation
- **Integration Tests** - Test repository layer with H2 database
- **MockMvc Tests** - Test REST endpoints without starting the server
- **Mockito** - Mock dependencies for isolated testing

## Future Enhancements

Potential improvements for the API:

- [ ] Add authentication and authorization (Spring Security)
- [ ] Implement pagination for transaction lists
- [ ] Add filtering and search capabilities
- [ ] Support for multiple currencies
- [ ] Transaction export to CSV/PDF
- [ ] Real-time notifications for large transactions
- [ ] Analytics dashboard
- [ ] Integration with real banking APIs

## Troubleshooting

### Application won't start
- Verify Java 21 is installed: `java -version`
- Check if port 8080 is available
- Ensure Maven dependencies are downloaded: `mvn clean install`

### Tests failing
- Clean and rebuild: `mvn clean test`
- Verify Java 21 is being used
- Check for proper test annotations

## Support

For issues or questions, please create an issue in the repository.

## License

This project is available for educational and demonstration purposes.

## Author

Created as a demonstration of Spring Boot REST API development with comprehensive testing

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
