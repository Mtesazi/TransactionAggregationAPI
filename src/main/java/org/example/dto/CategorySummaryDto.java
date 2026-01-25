package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for category-specific transaction summary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaryDto {

    private String category;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private BigDecimal averageAmount;
}
