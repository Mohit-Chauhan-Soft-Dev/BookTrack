package com.booktrack.inventory.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.inventory.dto.request.CreateInventoryTransactionRequest;
import com.booktrack.inventory.dto.response.InventoryTransactionResponse;
import com.booktrack.inventory.enums.InventoryTransactionType;

public interface InventoryService {

    InventoryTransactionResponse createTransaction(
            CreateInventoryTransactionRequest request
    );

    InventoryTransactionResponse getTransactionById(
            Long id
    );

    PageResponse<InventoryTransactionResponse> getAllTransactions(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<InventoryTransactionResponse> getTransactionsByBookCopy(
            Long bookCopyId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<InventoryTransactionResponse> getTransactionsByType(
            InventoryTransactionType type,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}