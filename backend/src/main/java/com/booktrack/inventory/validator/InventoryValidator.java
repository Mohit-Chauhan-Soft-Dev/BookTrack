package com.booktrack.inventory.validator;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.exception.BadRequestException;
import com.booktrack.inventory.enums.InventoryTransactionType;
import org.springframework.stereotype.Component;

@Component
public class InventoryValidator {

    public void validateTransaction(
            BookCopy bookCopy,
            InventoryTransactionType type) {

        if (!bookCopy.isActive()) {
            throw new BadRequestException(
                    "Cannot create inventory transaction for an inactive book copy.");
        }

        if (type == null) {
            throw new BadRequestException(
                    "Inventory transaction type is required.");
        }
    }

    public void validateAcquisition(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BadRequestException(
                    "Only an available book copy can be recorded as acquired.");
        }
    }

    public void validateDamage(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BadRequestException(
                    "Only an available book copy can be marked as damaged.");
        }
    }

    public void validateLoss(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BadRequestException(
                    "Only an available book copy can be marked as lost.");
        }
    }

    public void validateFound(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() != BookCopyStatus.LOST) {
            throw new BadRequestException(
                    "Only a lost book copy can be marked as found."
            );
        }    
    }

    public void validateMaintenance(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() == BookCopyStatus.BORROWED) {
            throw new BadRequestException(
                    "Borrowed book copy cannot be moved to maintenance."
            );
        }

        if (bookCopy.getStatus() == BookCopyStatus.LOST) {
            throw new BadRequestException(
                    "Lost book copy cannot be moved to maintenance."
            );
        }    

        if (bookCopy.getStatus() == BookCopyStatus.MAINTENANCE) {
            throw new BadRequestException(
                    "Book copy is already in maintenance."
            );
        }
    }

    public void validateReturnFromMaintenance(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() != BookCopyStatus.MAINTENANCE) {
            throw new BadRequestException(
                    "Book copy is not currently in maintenance.");
        }
    }

    public void validateDisposal(
            BookCopy bookCopy) {

        if (bookCopy.getStatus() == BookCopyStatus.BORROWED) {
            throw new BadRequestException(
                    "Borrowed book copy cannot be disposed.");
        }

        if (bookCopy.getStatus() == BookCopyStatus.RESERVED) {
            throw new BadRequestException(
                    "Reserved book copy cannot be disposed.");
        }

        if (!bookCopy.isActive()) {
            throw new BadRequestException(
                    "Book copy is already inactive.");
        }
    }

}