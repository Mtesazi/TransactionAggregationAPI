package org.example.service;

import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import java.util.Optional;

public interface CategorizationService {

    // Old method (optional)
    Transaction categorizeTransaction(Transaction transaction, TransactionCategory category);

    // New method for controller convenience
    Optional<Transaction> categorizeTransactionById(String transactionId, TransactionCategory category);
}
