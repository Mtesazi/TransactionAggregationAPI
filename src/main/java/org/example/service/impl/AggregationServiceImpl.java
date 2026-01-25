package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.Transaction;
import org.example.model.AggregatedSummary;
import org.example.repository.TransactionRepository;
import org.example.service.AggregationService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> aggregateTransactions(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    @Override
    public AggregatedSummary getSummary() {

        List<Transaction> transactions = transactionRepository.findAll();

        // Total amount
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Totals per category
        Map<String, BigDecimal> categoryTotals = transactions.stream()
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        return new AggregatedSummary(total, categoryTotals);
    }
}
