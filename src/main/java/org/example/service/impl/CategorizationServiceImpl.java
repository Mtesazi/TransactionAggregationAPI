package org.example.service.impl;

import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.repository.TransactionRepository;
import org.example.service.CategorizationService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategorizationServiceImpl implements CategorizationService {

    private final TransactionRepository transactionRepository;

    public CategorizationServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction categorizeTransaction(Transaction transaction, TransactionCategory category) {
        transaction.setCategory(category);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> categorizeTransactionById(String transactionId, TransactionCategory category) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        transactionOpt.ifPresent(tx -> tx.setCategory(category));
        transactionOpt.ifPresent(transactionRepository::save);
        return transactionOpt;
    }
}
