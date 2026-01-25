package org.example.service;

import org.example.model.AggregatedSummary;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.repository.TransactionRepository;
import org.example.service.impl.AggregationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AggregationServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AggregationServiceImpl aggregationService;

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction1 = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        transaction2 = new Transaction(
                "2",
                "customer-123",
                "Restaurant",
                new BigDecimal("30.00"),
                LocalDate.of(2024, 1, 16),
                TransactionCategory.FOOD
        );

        transaction3 = new Transaction(
                "3",
                "customer-456",
                "Gas Station",
                new BigDecimal("40.00"),
                LocalDate.of(2024, 1, 17),
                TransactionCategory.TRAVEL
        );
    }

    @Test
    void aggregateTransactions_WithValidCustomerId_ShouldReturnCustomerTransactions() {
        // Given
        String customerId = "customer-123";
        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(expectedTransactions);

        // When
        List<Transaction> result = aggregationService.aggregateTransactions(customerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("customer-123", result.get(0).getCustomerId());
        assertEquals("customer-123", result.get(1).getCustomerId());
        verify(transactionRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void aggregateTransactions_WithNoTransactions_ShouldReturnEmptyList() {
        // Given
        String customerId = "customer-999";
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        // When
        List<Transaction> result = aggregationService.aggregateTransactions(customerId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void getSummary_WithMultipleTransactions_ShouldReturnCorrectAggregatedSummary() {
        // Given
        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2, transaction3);
        when(transactionRepository.findAll()).thenReturn(allTransactions);

        // When
        AggregatedSummary result = aggregationService.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("120.00"), result.getTotal());
        assertNotNull(result.getCategoryTotals());
        assertEquals(3, result.getCategoryTotals().size());
        assertEquals(new BigDecimal("50.00"), result.getCategoryTotals().get("GROCERIES"));
        assertEquals(new BigDecimal("30.00"), result.getCategoryTotals().get("FOOD"));
        assertEquals(new BigDecimal("40.00"), result.getCategoryTotals().get("TRAVEL"));
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getSummary_WithSameCategory_ShouldSumAmounts() {
        // Given
        Transaction transaction4 = new Transaction(
                "4",
                "customer-789",
                "Supermarket",
                new BigDecimal("75.00"),
                LocalDate.of(2024, 1, 18),
                TransactionCategory.GROCERIES
        );
        List<Transaction> transactions = Arrays.asList(transaction1, transaction4);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // When
        AggregatedSummary result = aggregationService.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("125.00"), result.getTotal());
        assertEquals(new BigDecimal("125.00"), result.getCategoryTotals().get("GROCERIES"));
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getSummary_WithNoTransactions_ShouldReturnZeroTotal() {
        // Given
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        AggregatedSummary result = aggregationService.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotal());
        assertNotNull(result.getCategoryTotals());
        assertTrue(result.getCategoryTotals().isEmpty());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getSummary_WithNullCategory_ShouldExcludeFromCategoryTotals() {
        // Given
        Transaction transactionWithoutCategory = new Transaction(
                "5",
                "customer-123",
                "Unknown",
                new BigDecimal("20.00"),
                LocalDate.of(2024, 1, 19),
                null
        );
        List<Transaction> transactions = Arrays.asList(transaction1, transactionWithoutCategory);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // When
        AggregatedSummary result = aggregationService.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("70.00"), result.getTotal());
        assertEquals(1, result.getCategoryTotals().size());
        assertEquals(new BigDecimal("50.00"), result.getCategoryTotals().get("GROCERIES"));
        assertNull(result.getCategoryTotals().get("null"));
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getSummary_WithAllCategories_ShouldCalculateCorrectly() {
        // Given
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1", "C1", "Food", new BigDecimal("10.00"), LocalDate.now(), TransactionCategory.FOOD),
                new Transaction("2", "C1", "Travel", new BigDecimal("20.00"), LocalDate.now(), TransactionCategory.TRAVEL),
                new Transaction("3", "C1", "Shopping", new BigDecimal("30.00"), LocalDate.now(), TransactionCategory.SHOPPING),
                new Transaction("4", "C1", "Utilities", new BigDecimal("40.00"), LocalDate.now(), TransactionCategory.UTILITIES),
                new Transaction("5", "C1", "Groceries", new BigDecimal("50.00"), LocalDate.now(), TransactionCategory.GROCERIES),
                new Transaction("6", "C1", "Entertainment", new BigDecimal("60.00"), LocalDate.now(), TransactionCategory.ENTERTAINMENT),
                new Transaction("7", "C1", "Other", new BigDecimal("70.00"), LocalDate.now(), TransactionCategory.OTHER)
        );
        when(transactionRepository.findAll()).thenReturn(transactions);

        // When
        AggregatedSummary result = aggregationService.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("280.00"), result.getTotal());
        assertEquals(7, result.getCategoryTotals().size());
        verify(transactionRepository, times(1)).findAll();
    }
}
