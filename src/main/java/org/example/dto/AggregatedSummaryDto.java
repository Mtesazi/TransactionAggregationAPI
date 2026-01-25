package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for transferring aggregated transaction summary data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedSummaryDto {

    private BigDecimal total;
    private Map<String, BigDecimal> categoryTotals;
    private Integer transactionCount;
    private String currency;

    public AggregatedSummaryDto(BigDecimal total, Map<String, BigDecimal> categoryTotals) {
        this.total = total;
        this.categoryTotals = categoryTotals;
        this.currency = "USD";
    }
}
