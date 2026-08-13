package com.booktrack.inventory.dto.request;

import com.booktrack.inventory.enums.InventoryTransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryTransactionRequest {

    @NotNull(message = "Book copy is required")
    private Long bookCopyId;

    @NotNull(message = "Transaction type is required")
    private InventoryTransactionType type;

    @Size(max = 500)
    private String reason;

    @Size(max = 1000)
    private String notes;
}