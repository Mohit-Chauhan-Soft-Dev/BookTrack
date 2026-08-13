package com.booktrack.inventory.dto.response;

import com.booktrack.inventory.enums.InventoryTransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransactionResponse {

    private Long id;

    private Long bookCopyId;

    private String barcode;

    private Long bookId;

    private String bookTitle;

    private InventoryTransactionType type;

    private String reason;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}