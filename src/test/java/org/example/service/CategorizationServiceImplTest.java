package org.example.service;

import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.repository.TransactionRepository;
import org.example.service.impl.CategorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategorizationServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CategorizationServiceImpl categorizationService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.OTHER
        );
    }

    @Test
    void categorizeTransaction_ShouldUpdateCategoryAndSave() {
        // Given
        TransactionCategory newCategory = TransactionCategory.GROCERIES;
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Transaction result = categorizationService.categorizeTransaction(transaction, newCategory);

        // Then
        assertNotNull(result);
        assertEquals(newCategory, result.getCategory());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransaction_WithDifferentCategories_ShouldUpdateCorrectly() {
        // Given
        TransactionCategory newCategory = TransactionCategory.FOOD;
        Transaction updatedTransaction = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                newCategory
        );
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        // When
        Transaction result = categorizationService.categorizeTransaction(transaction, newCategory);

        // Then
        assertNotNull(result);
        assertEquals(TransactionCategory.FOOD, transaction.getCategory());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithValidId_ShouldReturnCategorizedTransaction() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.GROCERIES;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(newCategory, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithInvalidId_ShouldReturnEmpty() {
        // Given
        String transactionId = "999";
        TransactionCategory newCategory = TransactionCategory.GROCERIES;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertFalse(result.isPresent());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void categorizeTransactionById_WithFoodCategory_ShouldUpdateToFood() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.FOOD;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(TransactionCategory.FOOD, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithTravelCategory_ShouldUpdateToTravel() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.TRAVEL;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(TransactionCategory.TRAVEL, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithShoppingCategory_ShouldUpdateToShopping() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.SHOPPING;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(TransactionCategory.SHOPPING, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithUtilitiesCategory_ShouldUpdateToUtilities() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.UTILITIES;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(TransactionCategory.UTILITIES, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void categorizeTransactionById_WithEntertainmentCategory_ShouldUpdateToEntertainment() {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.ENTERTAINMENT;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Optional<Transaction> result = categorizationService.categorizeTransactionById(transactionId, newCategory);

        // Then
        assertTrue(result.isPresent());
        assertEquals(TransactionCategory.ENTERTAINMENT, result.get().getCategory());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }
}
