package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(String.valueOf(id)).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found with id: " + id ));
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        return transactionRepository.save(transaction);
    }


    @Override
    public Transaction updateTransaction(String id, Transaction transaction) {

        // Find the existing transaction
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id ));

        // Update fields
        existing.setCustomerId(transaction.getCustomerId());
        existing.setDescription(transaction.getDescription());
        existing.setAmount(transaction.getAmount());
        existing.setDate(transaction.getDate());
        existing.setCategory(transaction.getCategory());

        // Save and return updated transaction
        return transactionRepository.save(existing);
    }

    @Override
    public void deleteTransaction(String id) {

        // Find the existing transaction
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        // Delete the transaction
        transactionRepository.delete(existing);

        // Return custom success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Resource deleted successfully");

    }

}