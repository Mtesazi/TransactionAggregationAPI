package org.example.service;

import org.example.model.AggregatedSummary;
import org.example.model.Transaction;
import java.util.List;

public interface AggregationService {

    List<Transaction> aggregateTransactions(String customerId);
    AggregatedSummary getSummary();
}
