package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for customer-specific transaction summary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDto {

    private String customerId;
    private Integer totalTransactions;
    private BigDecimal totalAmount;
    private BigDecimal averageTransactionAmount;
    private List<TransactionDto> recentTransactions;
}
