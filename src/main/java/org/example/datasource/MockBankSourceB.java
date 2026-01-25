package org.example.datasource;


import org.example.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MockBankSourceB {
    public List<Transaction> fetchTransactions(String customerId) {
        return List.of( new Transaction("B1", customerId, "Amazon",
                        new BigDecimal("120.00"), LocalDate.now().minusDays(3), null)
        );
    }
}