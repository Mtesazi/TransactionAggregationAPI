package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String id;
    private String customerId;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String category;

   }
