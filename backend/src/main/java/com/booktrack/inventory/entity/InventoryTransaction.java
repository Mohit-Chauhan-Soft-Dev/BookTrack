package com.booktrack.inventory.entity;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.common.entity.BaseEntity;
import com.booktrack.inventory.enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "book_copy_id",
            nullable = false
    )
    private BookCopy bookCopy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private InventoryTransactionType type;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String notes;
}