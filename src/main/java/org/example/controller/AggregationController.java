package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Transaction;
import org.example.model.AggregatedSummary;
import org.example.service.AggregationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/aggregation")
@RequiredArgsConstructor
public class AggregationController {

    private final AggregationService aggregationService;


    @GetMapping("/transactions/{customerId}")
    public List<Transaction> getTransactionsByCustomer(
            @PathVariable String customerId) {
        return aggregationService.aggregateTransactions(customerId);
    }

    @GetMapping("/summary")
    public AggregatedSummary getTransactionSummary() {
        return aggregationService.getSummary();
    }
}
