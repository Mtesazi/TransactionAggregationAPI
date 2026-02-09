package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TransactionDto;
import org.example.model.AggregatedSummary;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.AggregationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AggregationController.class)
class AggregationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AggregationService aggregationService;

    // Mock JwtUtil so security-related beans can be created during the test context load
    @MockBean
    private org.example.security.JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private List<TransactionDto> mockTransactionDtos;
    private AggregatedSummary mockSummary;

    @BeforeEach
    void setUp() {
        // Register modules so LocalDate serializes/deserializes correctly in tests
        objectMapper.findAndRegisterModules();

        TransactionDto transaction1 = new TransactionDto(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                "GROCERIES"
        );

        TransactionDto transaction2 = new TransactionDto(
                "2",
                "customer-123",
                "Restaurant",
                new BigDecimal("30.00"),
                LocalDate.of(2024, 1, 16),
                "FOOD"
        );

        mockTransactionDtos = Arrays.asList(transaction1, transaction2);

        // Setup mock summary
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        categoryTotals.put("GROCERIES", new BigDecimal("50.00"));
        categoryTotals.put("FOOD", new BigDecimal("30.00"));
        mockSummary = new AggregatedSummary(new BigDecimal("80.00"), categoryTotals);
    }

    // Helper to convert DTO -> domain Transaction for stubbing services
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
    void getTransactionsByCustomer_ShouldReturnTransactionsList() throws Exception {
        // Given
        List<Transaction> entities = mockTransactionDtos.stream().map(this::toEntity).collect(Collectors.toList());
        String customerId = "customer-123";
        when(aggregationService.aggregateTransactions(customerId)).thenReturn(entities);

        // When & Then
        mockMvc.perform(get("/aggregation/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(mockTransactionDtos.get(0).getId())))
                .andExpect(jsonPath("$[0].customerId", is(mockTransactionDtos.get(0).getCustomerId())))
                .andExpect(jsonPath("$[0].description", is(mockTransactionDtos.get(0).getDescription())))
                .andExpect(jsonPath("$[0].amount", is(50.00)))
                .andExpect(jsonPath("$[0].category", is(mockTransactionDtos.get(0).getCategory())))
                .andExpect(jsonPath("$[1].id", is(mockTransactionDtos.get(1).getId())))
                .andExpect(jsonPath("$[1].customerId", is(mockTransactionDtos.get(1).getCustomerId())))
                .andExpect(jsonPath("$[1].description", is(mockTransactionDtos.get(1).getDescription())))
                .andExpect(jsonPath("$[1].amount", is(30.00)))
                .andExpect(jsonPath("$[1].category", is(mockTransactionDtos.get(1).getCategory())));
    }

    @Test
    void getTransactionsByCustomer_WithNoTransactions_ShouldReturnEmptyList() throws Exception {
        // Given
        String customerId = "customer-999";
        when(aggregationService.aggregateTransactions(customerId)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/aggregation/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getTransactionSummary_ShouldReturnAggregatedSummary() throws Exception {
        // Given
        when(aggregationService.getSummary()).thenReturn(mockSummary);

        // When & Then
        mockMvc.perform(get("/aggregation/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total", is(0)))
                .andExpect(jsonPath("$.categoryTotals", anEmptyMap()));
    }
}
