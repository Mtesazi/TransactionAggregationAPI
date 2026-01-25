package org.example.mapper;

import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;


public class TransactionMapper {

    public static TransactionDto mapToDto(Transaction transaction) {
           return new TransactionDto(
                transaction.getId(),
                transaction.getCustomerId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getCategory() != null ? transaction.getCategory().name() : null
        );
    }


    public static Transaction mapToEntity(TransactionDto dto) {

        TransactionCategory category = null;
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            category = TransactionCategory.valueOf(dto.getCategory().toUpperCase());
        }
        return new Transaction(
                dto.getId(),
                dto.getCustomerId(),
                dto.getDescription(),
                dto.getAmount(),
                dto.getDate(),
                category
        );
    }
}
