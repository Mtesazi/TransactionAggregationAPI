package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Transaction t = new Transaction();

        // Then
        assertNotNull(t);
        assertNull(t.getId());
        assertNull(t.getCustomerId());
        assertNull(t.getDescription());
        assertNull(t.getAmount());
        assertNull(t.getDate());
        assertNull(t.getCategory());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        String id = "123";
        String customerId = "customer-456";
        String description = "Test Transaction";
        BigDecimal amount = new BigDecimal("100.50");
        LocalDate date = LocalDate.of(2024, 1, 15);
        TransactionCategory category = TransactionCategory.GROCERIES;

        // When
        Transaction t = new Transaction(id, customerId, description, amount, date, category);

        // Then
        assertNotNull(t);
        assertEquals(id, t.getId());
        assertEquals(customerId, t.getCustomerId());
        assertEquals(description, t.getDescription());
        assertEquals(amount, t.getAmount());
        assertEquals(date, t.getDate());
        assertEquals(category, t.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        String id = "test-id-123";
        String customerId = "customer-789";
        String description = "Grocery Shopping";
        BigDecimal amount = new BigDecimal("250.75");
        LocalDate date = LocalDate.of(2024, 2, 20);
        TransactionCategory category = TransactionCategory.FOOD;

        // When
        transaction.setId(id);
        transaction.setCustomerId(customerId);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setCategory(category);

        // Then
        assertEquals(id, transaction.getId());
        assertEquals(customerId, transaction.getCustomerId());
        assertEquals(description, transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
        assertEquals(date, transaction.getDate());
        assertEquals(category, transaction.getCategory());
    }

    @Test
    void testSetId() {
        // Given
        String id = "unique-id-001";

        // When
        transaction.setId(id);

        // Then
        assertEquals(id, transaction.getId());
    }

    @Test
    void testSetCustomerId() {
        // Given
        String customerId = "customer-999";

        // When
        transaction.setCustomerId(customerId);

        // Then
        assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    void testSetDescription() {
        // Given
        String description = "Online Shopping";

        // When
        transaction.setDescription(description);

        // Then
        assertEquals(description, transaction.getDescription());
    }

    @Test
    void testSetAmount() {
        // Given
        BigDecimal amount = new BigDecimal("500.00");

        // When
        transaction.setAmount(amount);

        // Then
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void testSetAmountWithDecimals() {
        // Given
        BigDecimal amount = new BigDecimal("123.45");

        // When
        transaction.setAmount(amount);

        // Then
        assertEquals(amount, transaction.getAmount());
        assertEquals(0, amount.compareTo(transaction.getAmount()));
    }

    @Test
    void testSetDate() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 31);

        // When
        transaction.setDate(date);

        // Then
        assertEquals(date, transaction.getDate());
    }

    @Test
    void testSetCategory() {
        // Given
        TransactionCategory category = TransactionCategory.ENTERTAINMENT;

        // When
        transaction.setCategory(category);

        // Then
        assertEquals(category, transaction.getCategory());
    }

    @Test
    void testSetAllCategories() {
        // Test FOOD
        transaction.setCategory(TransactionCategory.FOOD);
        assertEquals(TransactionCategory.FOOD, transaction.getCategory());

        // Test TRAVEL
        transaction.setCategory(TransactionCategory.TRAVEL);
        assertEquals(TransactionCategory.TRAVEL, transaction.getCategory());

        // Test SHOPPING
        transaction.setCategory(TransactionCategory.SHOPPING);
        assertEquals(TransactionCategory.SHOPPING, transaction.getCategory());

        // Test UTILITIES
        transaction.setCategory(TransactionCategory.UTILITIES);
        assertEquals(TransactionCategory.UTILITIES, transaction.getCategory());

        // Test GROCERIES
        transaction.setCategory(TransactionCategory.GROCERIES);
        assertEquals(TransactionCategory.GROCERIES, transaction.getCategory());

        // Test ENTERTAINMENT
        transaction.setCategory(TransactionCategory.ENTERTAINMENT);
        assertEquals(TransactionCategory.ENTERTAINMENT, transaction.getCategory());

        // Test OTHER
        transaction.setCategory(TransactionCategory.OTHER);
        assertEquals(TransactionCategory.OTHER, transaction.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Transaction t1 = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        Transaction t2 = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        Transaction t3 = new Transaction(
                "2",
                "customer-456",
                "Gas Station",
                new BigDecimal("40.00"),
                LocalDate.of(2024, 1, 16),
                TransactionCategory.TRAVEL
        );

        // Then
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testEqualsWithSameObject() {
        // Then
        assertEquals(transaction, transaction);
    }

    @Test
    void testEqualsWithNull() {
        // Then
        assertNotEquals(null, transaction);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String notATransaction = "I am not a transaction";

        // Then
        assertNotEquals(transaction, notATransaction);
    }

    @Test
    void testToString() {
        // Given
        transaction.setId("123");
        transaction.setCustomerId("customer-456");
        transaction.setDescription("Test Description");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.of(2024, 1, 15));
        transaction.setCategory(TransactionCategory.FOOD);

        // When
        String result = transaction.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Transaction"));
    }

    @Test
    void testTransactionWithNullValues() {
        // Given & When
        transaction.setId(null);
        transaction.setCustomerId(null);
        transaction.setDescription(null);
        transaction.setAmount(null);
        transaction.setDate(null);
        transaction.setCategory(null);

        // Then
        assertNull(transaction.getId());
        assertNull(transaction.getCustomerId());
        assertNull(transaction.getDescription());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDate());
        assertNull(transaction.getCategory());
    }

    @Test
    void testTransactionWithZeroAmount() {
        // Given
        BigDecimal zero = BigDecimal.ZERO;

        // When
        transaction.setAmount(zero);

        // Then
        assertEquals(zero, transaction.getAmount());
        assertEquals(0, transaction.getAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testTransactionWithNegativeAmount() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-50.00");

        // When
        transaction.setAmount(negativeAmount);

        // Then
        assertEquals(negativeAmount, transaction.getAmount());
        assertTrue(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testTransactionWithFutureDate() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(30);

        // When
        transaction.setDate(futureDate);

        // Then
        assertEquals(futureDate, transaction.getDate());
        assertTrue(transaction.getDate().isAfter(LocalDate.now()));
    }

    @Test
    void testTransactionWithPastDate() {
        // Given
        LocalDate pastDate = LocalDate.now().minusYears(1);

        // When
        transaction.setDate(pastDate);

        // Then
        assertEquals(pastDate, transaction.getDate());
        assertTrue(transaction.getDate().isBefore(LocalDate.now()));
    }
}
