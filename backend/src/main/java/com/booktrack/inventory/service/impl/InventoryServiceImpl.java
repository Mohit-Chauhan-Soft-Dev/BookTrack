package com.booktrack.inventory.service.impl;

import com.booktrack.book.entity.Book;
import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.bookcopy.repository.BookCopyRepository;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.inventory.dto.request.CreateInventoryTransactionRequest;
import com.booktrack.inventory.dto.response.InventoryTransactionResponse;
import com.booktrack.inventory.entity.InventoryTransaction;
import com.booktrack.inventory.enums.InventoryTransactionType;
import com.booktrack.inventory.mapper.InventoryTransactionMapper;
import com.booktrack.inventory.repository.InventoryTransactionRepository;
import com.booktrack.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booktrack.inventory.validator.InventoryValidator;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

        private final InventoryTransactionRepository inventoryTransactionRepository;
        private final BookCopyRepository bookCopyRepository;
        private final InventoryTransactionMapper inventoryTransactionMapper;
        private final InventoryValidator inventoryValidator;

        @Override
        @Transactional
        public InventoryTransactionResponse createTransaction(
                        CreateInventoryTransactionRequest request) {

                BookCopy bookCopy = bookCopyRepository.findById(request.getBookCopyId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Book copy not found: "
                                                                + request.getBookCopyId()));

                inventoryValidator.validateTransaction(
                                bookCopy,
                                request.getType());

                applyInventoryStateChange(
                                bookCopy,
                                request.getType());

                InventoryTransaction transaction = InventoryTransaction.builder()
                                .bookCopy(bookCopy)
                                .type(request.getType())
                                .reason(request.getReason())
                                .notes(request.getNotes())
                                .build();

                InventoryTransaction saved = inventoryTransactionRepository.save(transaction);

                return inventoryTransactionMapper.toResponse(saved);
        }

        private void applyInventoryStateChange(
                        BookCopy bookCopy,
                        InventoryTransactionType type) {

                Book book = bookCopy.getBook();

                BookCopyStatus oldStatus = bookCopy.getStatus();

                BookCopyStatus newStatus;

                switch (type) {

                        case ACQUISITION -> {
                                inventoryValidator.validateAcquisition(
                                                bookCopy);

                                newStatus = BookCopyStatus.AVAILABLE;
                        }

                        case DAMAGE -> {
                                inventoryValidator.validateDamage(
                                                bookCopy);

                                newStatus = BookCopyStatus.DAMAGED;
                        }

                        case LOSS -> {
                                inventoryValidator.validateLoss(
                                                bookCopy);

                                newStatus = BookCopyStatus.LOST;
                        }

                        case FOUND -> {
                                inventoryValidator.validateFound(
                                                bookCopy);

                                newStatus = BookCopyStatus.AVAILABLE;
                        }

                        case MAINTENANCE -> {
                                inventoryValidator.validateMaintenance(
                                                bookCopy);

                                newStatus = BookCopyStatus.MAINTENANCE;
                        }

                        case RETURN_FROM_MAINTENANCE -> {
                                inventoryValidator.validateReturnFromMaintenance(
                                                bookCopy);

                                newStatus = BookCopyStatus.AVAILABLE;
                        }

                        case DISPOSAL -> {
                                inventoryValidator.validateDisposal(
                                                bookCopy);

                                book.setTotalCopies(
                                                book.getTotalCopies() - 1);

                                if (oldStatus == BookCopyStatus.AVAILABLE) {

                                        if (book.getAvailableCopies() <= 0) {
                                                throw new BadRequestException(
                                                                "Book available copy count cannot become negative.");
                                        }

                                        book.setAvailableCopies(
                                                        book.getAvailableCopies() - 1);
                                }

                                bookCopy.setActive(false);

                                newStatus = oldStatus;
                        }

                        case ADJUSTMENT -> {
                                throw new BadRequestException(
                                                "Inventory adjustment requires an explicit state change.");
                        }

                        default -> throw new BadRequestException(
                                        "Unsupported inventory transaction type.");
                }

                if (type != InventoryTransactionType.DISPOSAL) {

                        updateAvailableCopies(
                                        book,
                                        oldStatus,
                                        newStatus);
                }

                bookCopy.setStatus(newStatus);

                bookCopyRepository.save(bookCopy);
        }

        private void updateAvailableCopies(
                        Book book,
                        BookCopyStatus oldStatus,
                        BookCopyStatus newStatus) {

                boolean wasAvailable = oldStatus == BookCopyStatus.AVAILABLE;

                boolean becomesAvailable = newStatus == BookCopyStatus.AVAILABLE;

                if (wasAvailable && !becomesAvailable) {

                        if (book.getAvailableCopies() <= 0) {
                                throw new BadRequestException(
                                                "Book available copy count cannot become negative.");
                        }

                        book.setAvailableCopies(
                                        book.getAvailableCopies() - 1);
                }

                if (!wasAvailable && becomesAvailable) {

                        book.setAvailableCopies(
                                        book.getAvailableCopies() + 1);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public InventoryTransactionResponse getTransactionById(
                        Long id) {

                InventoryTransaction transaction = inventoryTransactionRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Inventory transaction not found: "
                                                                + id));

                return inventoryTransactionMapper.toResponse(transaction);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<InventoryTransactionResponse> getAllTransactions(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<InventoryTransaction> transactionPage = inventoryTransactionRepository.findAll(pageable);

                return createPageResponse(transactionPage);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<InventoryTransactionResponse> getTransactionsByBookCopy(
                        Long bookCopyId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Book copy not found: "
                                                                + bookCopyId));

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<InventoryTransaction> transactionPage = inventoryTransactionRepository.findByBookCopy(
                                bookCopy,
                                pageable);

                return createPageResponse(transactionPage);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<InventoryTransactionResponse> getTransactionsByType(
                        InventoryTransactionType type,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<InventoryTransaction> transactionPage = inventoryTransactionRepository.findByType(
                                type,
                                pageable);

                return createPageResponse(transactionPage);
        }

        private PageResponse<InventoryTransactionResponse> createPageResponse(
                        Page<InventoryTransaction> page) {

                return PageResponse.<InventoryTransactionResponse>builder()
                                .content(
                                                page.getContent()
                                                                .stream()
                                                                .map(inventoryTransactionMapper::toResponse)
                                                                .toList())
                                .page(page.getNumber())
                                .size(page.getSize())
                                .totalElements(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .first(page.isFirst())
                                .last(page.isLast())
                                .build();
        }
}