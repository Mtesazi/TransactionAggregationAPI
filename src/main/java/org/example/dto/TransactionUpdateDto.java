package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating existing transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateDto {

    private String customerId;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String category;
}
