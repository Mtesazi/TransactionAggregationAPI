package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating new transactions (excludes ID)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateDto {

    private String customerId;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String category;
}
