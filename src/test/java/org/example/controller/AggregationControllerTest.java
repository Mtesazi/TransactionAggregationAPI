package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.AggregatedSummary;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.AggregationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AggregationController.class)
class AggregationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AggregationService aggregationService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Transaction> mockTransactions;
    private AggregatedSummary mockSummary;

    @BeforeEach
    void setUp() {
        // Setup mock transactions
        Transaction transaction1 = new Transaction(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                TransactionCategory.GROCERIES
        );

        Transaction transaction2 = new Transaction(
                "2",
                "customer-123",
                "Restaurant",
                new BigDecimal("30.00"),
                LocalDate.of(2024, 1, 16),
                TransactionCategory.FOOD
        );

        mockTransactions = Arrays.asList(transaction1, transaction2);

        // Setup mock summary
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        categoryTotals.put("GROCERIES", new BigDecimal("50.00"));
        categoryTotals.put("FOOD", new BigDecimal("30.00"));
        mockSummary = new AggregatedSummary(new BigDecimal("80.00"), categoryTotals);
    }

    @Test
    void getTransactionsByCustomer_ShouldReturnTransactionsList() throws Exception {
        // Given
        String customerId = "customer-123";
        when(aggregationService.aggregateTransactions(customerId)).thenReturn(mockTransactions);

        // When & Then
        mockMvc.perform(get("/aggregation/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].customerId", is("customer-123")))
                .andExpect(jsonPath("$[0].description", is("Grocery Store")))
                .andExpect(jsonPath("$[0].amount", is(50.00)))
                .andExpect(jsonPath("$[0].category", is("GROCERIES")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].customerId", is("customer-123")))
                .andExpect(jsonPath("$[1].description", is("Restaurant")))
                .andExpect(jsonPath("$[1].amount", is(30.00)))
                .andExpect(jsonPath("$[1].category", is("FOOD")));
    }

    @Test
    void getTransactionsByCustomer_WithNoTransactions_ShouldReturnEmptyList() throws Exception {
        // Given
        String customerId = "customer-999";
        when(aggregationService.aggregateTransactions(customerId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/aggregation/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getTransactionSummary_ShouldReturnAggregatedSummary() throws Exception {
        // Given
        when(aggregationService.getSummary()).thenReturn(mockSummary);

        // When & Then
        mockMvc.perform(get("/aggregation/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total", is(80.00)))
                .andExpect(jsonPath("$.categoryTotals.GROCERIES", is(50.00)))
                .andExpect(jsonPath("$.categoryTotals.FOOD", is(30.00)));
    }

    @Test
    void getTransactionSummary_WithEmptyData_ShouldReturnZeroTotal() throws Exception {
        // Given
        AggregatedSummary emptySummary = new AggregatedSummary(
                BigDecimal.ZERO,
                new HashMap<>()
        );
        when(aggregationService.getSummary()).thenReturn(emptySummary);

        // When & Then
        mockMvc.perform(get("/aggregation/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total", is(0)))
                .andExpect(jsonPath("$.categoryTotals", anEmptyMap()));
    }
}
