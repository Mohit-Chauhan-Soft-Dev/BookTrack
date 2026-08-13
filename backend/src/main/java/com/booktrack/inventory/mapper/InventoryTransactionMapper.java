package com.booktrack.inventory.mapper;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.inventory.dto.response.InventoryTransactionResponse;
import com.booktrack.inventory.entity.InventoryTransaction;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper {

    public InventoryTransactionResponse toResponse(
            InventoryTransaction transaction) {

        BookCopy bookCopy = transaction.getBookCopy();

        return InventoryTransactionResponse.builder()
                .id(transaction.getId())
                .bookCopyId(bookCopy.getId())
                .barcode(bookCopy.getBarcode())
                .bookId(bookCopy.getBook().getId())
                .bookTitle(bookCopy.getBook().getTitle())
                .type(transaction.getType())
                .reason(transaction.getReason())
                .notes(transaction.getNotes())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}