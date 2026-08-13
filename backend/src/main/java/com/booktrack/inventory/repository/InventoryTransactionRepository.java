package com.booktrack.inventory.repository;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.inventory.entity.InventoryTransaction;
import com.booktrack.inventory.enums.InventoryTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {

    Page<InventoryTransaction> findByBookCopy(
            BookCopy bookCopy,
            Pageable pageable
    );

    Page<InventoryTransaction> findByType(
            InventoryTransactionType type,
            Pageable pageable
    );

    List<InventoryTransaction> findByBookCopyOrderByCreatedAtDesc(
            BookCopy bookCopy
    );
}