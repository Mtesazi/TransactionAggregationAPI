package org.example.service;

import org.example.model.Transaction;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TransactionService {

    List<Transaction> getAllTransactions();
    Transaction getTransactionById(String id);
    Transaction createTransaction(Transaction transaction);
    Transaction updateTransaction(String id, Transaction transaction);
    void deleteTransaction(String id);
}
