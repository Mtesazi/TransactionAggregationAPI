package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.CategorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CategorizationController.class)
class CategorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorizationService categorizationService;

    @MockBean
    private org.example.security.JwtUtil jwtUtil;


    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDto mockTransactionDto;

    @BeforeEach
    void setUp() {
        // Ensure ObjectMapper handles Java 8 date/time types
        objectMapper.findAndRegisterModules();

        mockTransactionDto = new TransactionDto(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                "GROCERIES"
        );
    }

    // Helper to convert DTO -> domain Transaction
    private Transaction toEntity(TransactionDto dto) {
        return new Transaction(
                dto.getId(),
                dto.getCustomerId(),
                dto.getDescription(),
                dto.getAmount(),
                dto.getDate(),
                TransactionCategory.valueOf(dto.getCategory())
        );
    }

    @Test
    void categorizeTransaction_WithValidId_ShouldReturnCategorizedTransaction() throws Exception {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.FOOD;

        TransactionDto categorizedDto = new TransactionDto(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                "FOOD"
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(toEntity(categorizedDto)));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(categorizedDto.getId())))
                .andExpect(jsonPath("$.customerId", is(categorizedDto.getCustomerId())))
                .andExpect(jsonPath("$.description", is(categorizedDto.getDescription())))
                .andExpect(jsonPath("$.amount", is(50.00)))
                .andExpect(jsonPath("$.category", is(categorizedDto.getCategory())));
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

        TransactionDto categorizedDto = new TransactionDto(
                "1",
                "customer-123",
                "Movie Theater",
                new BigDecimal("25.00"),
                LocalDate.of(2024, 1, 15),
                "ENTERTAINMENT"
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(toEntity(categorizedDto)));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category", is(categorizedDto.getCategory())));
    }

    @Test
    void categorizeTransaction_WithUtilitiesCategory_ShouldSucceed() throws Exception {
        // Given
        String transactionId = "1";
        TransactionCategory newCategory = TransactionCategory.UTILITIES;

        TransactionDto categorizedDto = new TransactionDto(
                "1",
                "customer-123",
                "Electric Bill",
                new BigDecimal("100.00"),
                LocalDate.of(2024, 1, 15),
                "UTILITIES"
        );

        when(categorizationService.categorizeTransactionById(eq(transactionId), eq(newCategory)))
                .thenReturn(Optional.of(toEntity(categorizedDto)));

        // When & Then
        mockMvc.perform(post("/transactions/{transactionId}/categorize", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category", is(categorizedDto.getCategory())));
    }
}
