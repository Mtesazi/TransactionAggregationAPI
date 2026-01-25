package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.CategorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategorizationController.class)
class CategorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorizationService categorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction mockTransaction;

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
    }

    @Test
    void categorizeTransaction_WithValidId_ShouldReturnCategorizedTransaction() throws Exception {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.FOOD;
        Transaction categorizedTransaction = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                newCategory
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(categorizedTransaction));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.customerId", is("customer-123")))
                .andExpect(jsonPath("$.description", is("Grocery Store")))
                .andExpect(jsonPath("$.amount", is(50.00)))
                .andExpect(jsonPath("$.category", is("FOOD")));
    }

    @Test
    void categorizeTransaction_WithInvalidId_ShouldThrowResourceNotFoundException() throws Exception {
        // Given
        String transactionId = "999";
        TransactionCategory newCategory = TransactionCategory.FOOD;

        when(categorizationService.categorizeTransactionById(eq(transactionId), any(TransactionCategory.class)))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isNotFound());
    }

    @Test
    void categorizeTransaction_WithDifferentCategories_ShouldUpdateCategory() throws Exception {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.ENTERTAINMENT;
        Transaction categorizedTransaction = new Transaction(
                "1",
                "customer-123",
                "Movie Theater",
                new BigDecimal("25.00"),
                LocalDate.of(2024, 1, 15),
                newCategory
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(categorizedTransaction));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.category", is("ENTERTAINMENT")));
    }

    @Test
    void categorizeTransaction_WithUtilitiesCategory_ShouldSucceed() throws Exception {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.UTILITIES;
        Transaction categorizedTransaction = new Transaction(
                "1",
                "customer-123",
                "Electric Bill",
                new BigDecimal("100.00"),
                LocalDate.of(2024, 1, 15),
                newCategory
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(categorizedTransaction));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.category", is("UTILITIES")));
    }
}
