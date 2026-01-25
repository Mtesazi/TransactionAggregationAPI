package org.example.controller;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;
import org.example.service.CategorizationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class CategorizationController {

    private final CategorizationService categorizationService;

    public CategorizationController(CategorizationService categorizationService) {
        this.categorizationService = categorizationService;
    }

    @PostMapping("/{transactionId}/categorize")
    public Transaction categorizeTransaction(
            @PathVariable String transactionId,
            @RequestBody TransactionCategory category
    ) {
        return (Transaction) categorizationService.categorizeTransactionById(transactionId, category)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID " + transactionId));
    }
}
