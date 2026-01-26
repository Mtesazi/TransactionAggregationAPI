// java
package org.example.mapper;

import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionCategory;

public final class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionDto mapToDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        String category = transaction.getCategory() != null ? transaction.getCategory().name() : null;

        return new TransactionDto(
                transaction.getId(),
                transaction.getCustomerId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                category
        );
    }

    public static Transaction mapToEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }

        TransactionCategory category = null;
        String raw = dto.getCategory();
        if (raw != null) {
            raw = raw.trim();
            if (!raw.isBlank()) {
                try {
                    category = TransactionCategory.valueOf(raw.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                }
            }
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
