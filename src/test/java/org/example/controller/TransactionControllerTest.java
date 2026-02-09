package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TransactionDto;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.TransactionService;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;



    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDto mockTransactionDto;
    private List<TransactionDto> mockTransactionDtos;

    @BeforeEach
    void setUp() {
        // Ensure ObjectMapper can handle Java 8 date/time types
        objectMapper.findAndRegisterModules();

        mockTransactionDto = new TransactionDto(
                "1",
                "customer-123",
                "Grocery Store",
                new BigDecimal("50.00"),
                LocalDate.of(2024, 1, 15),
                "GROCERIES"
        );

        TransactionDto transactionDto2 = new TransactionDto(
                "2",
                "customer-456",
                "Gas Station",
                new BigDecimal("40.00"),
                LocalDate.of(2024, 1, 16),
                "TRAVEL"
        );

        mockTransactionDtos = Arrays.asList(mockTransactionDto, transactionDto2);
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
    void getAllTransactions_ShouldReturnAllTransactions() throws Exception {
        // Given
        List<Transaction> entities = mockTransactionDtos.stream().map(this::toEntity).collect(Collectors.toList());
        when(transactionService.getAllTransactions()).thenReturn(entities);

        // When & Then
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(mockTransactionDtos.get(0).getId())))
                .andExpect(jsonPath("$[0].customerId", is(mockTransactionDtos.get(0).getCustomerId())))
                .andExpect(jsonPath("$[0].description", is(mockTransactionDtos.get(0).getDescription())))
                .andExpect(jsonPath("$[0].amount", is(50.00)))
                .andExpect(jsonPath("$[1].id", is(mockTransactionDtos.get(1).getId())))
                .andExpect(jsonPath("$[1].customerId", is(mockTransactionDtos.get(1).getCustomerId())))
                .andExpect(jsonPath("$[1].description", is(mockTransactionDtos.get(1).getDescription())));
    }

    @Test
    void getAllTransactions_WithNoTransactions_ShouldReturnEmptyList() throws Exception {
        // Given
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getTransactionById_WithValidId_ShouldReturnTransaction() throws Exception {
        // Given
        String transactionId = "1";
        when(transactionService.getTransactionById(transactionId)).thenReturn(toEntity(mockTransactionDto));

        // When & Then
        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(mockTransactionDto.getId())))
                .andExpect(jsonPath("$.customerId", is(mockTransactionDto.getCustomerId())))
                .andExpect(jsonPath("$.description", is(mockTransactionDto.getDescription())))
                .andExpect(jsonPath("$.amount", is(50.00)))
                .andExpect(jsonPath("$.category", is(mockTransactionDto.getCategory())));
    }

    @Test
    void getTransactionById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String transactionId = "999";
        when(transactionService.getTransactionById(transactionId))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        // When & Then
        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_WithValidData_ShouldReturnCreatedTransaction() throws Exception {
        // Given
        TransactionDto newDto = new TransactionDto(
                null,
                "customer-789",
                "Online Shopping",
                new BigDecimal("75.00"),
                LocalDate.of(2024, 1, 17),
                "SHOPPING"
        );

        TransactionDto createdDto = new TransactionDto(
                "3",
                newDto.getCustomerId(),
                newDto.getDescription(),
                newDto.getAmount(),
                newDto.getDate(),
                newDto.getCategory()
        );

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(toEntity(createdDto));

        // When & Then
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toEntity(newDto))))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdDto.getId())))
                .andExpect(jsonPath("$.customerId", is(createdDto.getCustomerId())))
                .andExpect(jsonPath("$.description", is(createdDto.getDescription())))
                .andExpect(jsonPath("$.amount", is(75.00)))
                .andExpect(jsonPath("$.category", is(createdDto.getCategory())));
    }

    @Test
    void updateTransaction_WithValidData_ShouldReturnUpdatedTransaction() throws Exception {
        // Given
        String transactionId = "1";
        TransactionDto updatedDto = new TransactionDto(
                "1",
                "customer-123",
                "Updated Grocery Store",
                new BigDecimal("60.00"),
                LocalDate.of(2024, 1, 15),
                "GROCERIES"
        );

        when(transactionService.updateTransaction(eq(transactionId), any(Transaction.class)))
                .thenReturn(toEntity(updatedDto));

        // When & Then
        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toEntity(updatedDto))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedDto.getId())))
                .andExpect(jsonPath("$.description", is(updatedDto.getDescription())))
                .andExpect(jsonPath("$.amount", is(60.00)));
    }

    @Test
    void updateTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String transactionId = "999";
        TransactionDto updatedDto = new TransactionDto(
                transactionId,
                "customer-123",
                "Updated Store",
                new BigDecimal("60.00"),
                LocalDate.of(2024, 1, 15),
                "GROCERIES"
        );

        when(transactionService.updateTransaction(eq(transactionId), any(Transaction.class)))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        // When & Then
        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toEntity(updatedDto))))
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
        doThrow(new ResourceNotFoundException("Transaction not found with id: " + transactionId))
                .when(transactionService).deleteTransaction(transactionId);

        // When & Then
        mockMvc.perform(delete("/transactions/{id}", transactionId))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}
