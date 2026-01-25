package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction mockTransaction;
    private List<Transaction> mockTransactions;

    @BeforeEach
    void setUp() {
        mockTransaction = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        Transaction transaction2 = new Transaction(
                "2",
                "customer-456",
                "Gas Station",
                new BigDecimal("40.00"),
                LocalDate.of(2024, 1, 16),
                TransactionCategory.TRAVEL
        );

        mockTransactions = Arrays.asList(mockTransaction, transaction2);
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() throws Exception {
        // Given
        when(transactionService.getAllTransactions()).thenReturn(mockTransactions);

        // When & Then
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].customerId", is("customer-123")))
                .andExpect(jsonPath("$[0].description", is("Grocery Store")))
                .andExpect(jsonPath("$[0].amount", is(50.00)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].customerId", is("customer-456")))
                .andExpect(jsonPath("$[1].description", is("Gas Station")));
    }

    @Test
    void getAllTransactions_WithNoTransactions_ShouldReturnEmptyList() throws Exception {
        // Given
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getTransactionById_WithValidId_ShouldReturnTransaction() throws Exception {
        // Given
        String transactionId = "1";
        when(transactionService.getTransactionById(transactionId)).thenReturn(mockTransaction);

        // When & Then
        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.customerId", is("customer-123")))
                .andExpect(jsonPath("$.description", is("Grocery Store")))
                .andExpect(jsonPath("$.amount", is(50.00)))
                .andExpect(jsonPath("$.category", is("GROCERIES")));
    }

    @Test
    void getTransactionById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String transactionId = "999";
        when(transactionService.getTransactionById(transactionId))
                .thenThrow(new ResourceNotFoundException("Transaction not found with ID " + transactionId));

        // When & Then
        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_WithValidData_ShouldReturnCreatedTransaction() throws Exception {
        // Given
        Transaction newTransaction = new Transaction(
                null,
                "customer-789",
                "Online Shopping",
                new BigDecimal("75.00"),
                LocalDate.of(2024, 1, 17),
                TransactionCategory.SHOPPING
        );

        Transaction createdTransaction = new Transaction(
                "3",
                "customer-789",
                "Online Shopping",
                new BigDecimal("75.00"),
                LocalDate.of(2024, 1, 17),
                TransactionCategory.SHOPPING
        );

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(createdTransaction);

        // When & Then
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("3")))
                .andExpect(jsonPath("$.customerId", is("customer-789")))
                .andExpect(jsonPath("$.description", is("Online Shopping")))
                .andExpect(jsonPath("$.amount", is(75.00)))
                .andExpect(jsonPath("$.category", is("SHOPPING")));
    }

    @Test
    void updateTransaction_WithValidData_ShouldReturnUpdatedTransaction() throws Exception {
        // Given
        String transactionId = "1";
        Transaction updatedData = new Transaction(
                "1",
                "customer-123",
                "Updated Grocery Store",
                new BigDecimal("60.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        when(transactionService.updateTransaction(eq(transactionId), any(Transaction.class)))
                .thenReturn(updatedData);

        // When & Then
        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.description", is("Updated Grocery Store")))
                .andExpect(jsonPath("$.amount", is(60.00)));
    }

    @Test
    void updateTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String transactionId = "999";
        Transaction updatedData = new Transaction(
                "999",
                "customer-123",
                "Updated Store",
                new BigDecimal("60.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        when(transactionService.updateTransaction(eq(transactionId), any(Transaction.class)))
                .thenThrow(new ResourceNotFoundException("Transaction not found with ID " + transactionId));

        // When & Then
        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTransaction_WithValidId_ShouldReturnNoContent() throws Exception {
        // Given
        String transactionId = "1";
        doNothing().when(transactionService).deleteTransaction(transactionId);

        // When & Then
        mockMvc.perform(delete("/transactions/{id}", transactionId))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void deleteTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String transactionId = "999";
        doThrow(new ResourceNotFoundException("Transaction not found with ID " + transactionId))
                .when(transactionService).deleteTransaction(transactionId);

        // When & Then
        mockMvc.perform(delete("/transactions/{id}", transactionId))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}
