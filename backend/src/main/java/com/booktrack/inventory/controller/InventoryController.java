package com.booktrack.inventory.controller;

import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.inventory.dto.request.CreateInventoryTransactionRequest;
import com.booktrack.inventory.dto.response.InventoryTransactionResponse;
import com.booktrack.inventory.enums.InventoryTransactionType;
import com.booktrack.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.INVENTORY)
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryTransactionResponse> createTransaction(
            @Valid @RequestBody CreateInventoryTransactionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        inventoryService.createTransaction(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryTransactionResponse> getTransactionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getTransactionById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<InventoryTransactionResponse>>
    getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                inventoryService.getAllTransactions(
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/book-copy/{bookCopyId}")
    public ResponseEntity<PageResponse<InventoryTransactionResponse>>
    getTransactionsByBookCopy(
            @PathVariable Long bookCopyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                inventoryService.getTransactionsByBookCopy(
                        bookCopyId,
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<PageResponse<InventoryTransactionResponse>>
    getTransactionsByType(
            @PathVariable InventoryTransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                inventoryService.getTransactionsByType(
                        type,
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }
}