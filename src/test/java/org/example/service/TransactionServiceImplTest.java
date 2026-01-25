package org.example.service;


import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.repository.TransactionRepository;
import org.example.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;



class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction = new Transaction();
        transaction.setId("1");
        transaction.setCustomerId("C123");
        transaction.setDescription("Test transaction");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setDate(LocalDate.now());
        transaction.setCategory(TransactionCategory.valueOf("FOOD"));
    }

    @Test
    void GetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));
        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(1, result.size());
        assertEquals(transaction.getId(), result.get(0).getId());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void GetTransactionById_Found() {
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));
        Transaction result = transactionService.getTransactionById("1");
        assertNotNull(result);
        assertEquals("C123", result.getCustomerId());
        verify(transactionRepository, times(1)).findById("1");
    }

    @Test
    void GetTransactionById_NotFound() {
        when(transactionRepository.findById("2")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionById("2"));
        assertEquals("Transaction not found with id: 2", exception.getMessage());
        verify(transactionRepository, times(1)).findById("2");
    }

    @Test
    void CreateTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction newTransaction = new Transaction();
        newTransaction.setCustomerId("C123");
        newTransaction.setDescription("Test transaction");
        newTransaction.setAmount(BigDecimal.valueOf(100.0));
        newTransaction.setDate(LocalDate.now());
        newTransaction.setCategory(TransactionCategory.valueOf("FOOD"));
        Transaction result = transactionService.createTransaction(newTransaction);
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void UpdateTransaction_Found() {
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setCustomerId("C999");
        updatedTransaction.setDescription("Updated transaction");
        updatedTransaction.setAmount(BigDecimal.valueOf(200.0));
        updatedTransaction.setDate(LocalDate.now());
        updatedTransaction.setCategory(TransactionCategory.FOOD);
        Transaction result = transactionService.updateTransaction("1", updatedTransaction);
        assertEquals("C999", result.getCustomerId());
        assertEquals("Updated transaction", result.getDescription());
        assertEquals(BigDecimal.valueOf(200.0), result.getAmount());
        assertEquals(TransactionCategory.FOOD, result.getCategory());
        verify(transactionRepository, times(1)).findById("1");
        verify(transactionRepository, times(1)).save(transaction);
    }


    @Test
    void UpdateTransaction_NotFound() {
        when(transactionRepository.findById("2")).thenReturn(Optional.empty());
        Transaction updatedTransaction = new Transaction();
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.updateTransaction("2", updatedTransaction));
        assertEquals("Transaction not found with id: 2", exception.getMessage());
        verify(transactionRepository, times(1)).findById("2");
    }

    @Test
    void DeleteTransaction_Found() {
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).delete(transaction);
        assertDoesNotThrow(() -> transactionService.deleteTransaction("1"));
        verify(transactionRepository, times(1)).findById("1");
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void DeleteTransaction_NotFound() {
        when(transactionRepository.findById("2")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.deleteTransaction("2"));
        assertEquals("Transaction not found with id: 2", exception.getMessage());
        verify(transactionRepository, times(1)).findById("2");
        verify(transactionRepository, never()).delete(any());
    }
}