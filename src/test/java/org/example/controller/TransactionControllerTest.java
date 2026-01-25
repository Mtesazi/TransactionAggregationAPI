package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

    private Transaction buildTransaction() {
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID().toString());
        t.setCustomerId("C123");
        t.setDescription("Test transaction");
        t.setAmount(BigDecimal.valueOf(100));
        t.setDate(LocalDate.now());
        t.setCategory(TransactionCategory.FOOD);
        return t;
    }

    @Test
    void getAllTransactions_success() throws Exception {
        when(transactionService.getAllTransactions())
                .thenReturn(List.of(buildTransaction()));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("C123"));
    }

    @Test
    void getTransactionById_success() throws Exception {
        Transaction transaction = buildTransaction();

        when(transactionService.getTransactionById(transaction.getId()))
                .thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C123"));
    }

    @Test
    void getTransactionById_notFound() throws Exception {
        when(transactionService.getTransactionById("1"))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id: 1"));

        mockMvc.perform(get("/transactions/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_success() throws Exception {
        Transaction transaction = buildTransaction();

        when(transactionService.createTransaction(any(Transaction.class)))
                .thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("C123"));
    }


    @Test
    void updateTransaction_success() throws Exception {
        Transaction transaction = buildTransaction();

        when(transactionService.updateTransaction(eq(transaction.getId()), any(Transaction.class)))
                .thenReturn(transaction);

        mockMvc.perform(put("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C123"));
    }

    @Test
    void deleteTransaction_success() throws Exception {
        doNothing().when(transactionService).deleteTransaction("1");

        mockMvc.perform(delete("/transactions/{id}", "1"))
                .andExpect(status().isNoContent());
    }

}
