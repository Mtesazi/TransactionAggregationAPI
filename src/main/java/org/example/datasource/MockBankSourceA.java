package org.example.datasource;

import org.example.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MockBankSourceA {

    public List<Transaction> fetchTransactions(String customerId) {
        return List.of(
                new Transaction("A1", customerId, "McDonalds", new BigDecimal("12.50"), LocalDate.now().minusDays(1), null),
                new Transaction("A2", customerId, "Uber", new BigDecimal("25.00"), LocalDate.now().minusDays(2), null)
        );
    }
}