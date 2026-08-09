package com.booktrack.bookcopy.mapper;

import com.booktrack.bookcopy.dto.request.CreateBookCopyRequest;
import com.booktrack.bookcopy.dto.request.UpdateBookCopyRequest;
import com.booktrack.bookcopy.dto.response.BookCopyResponse;
import com.booktrack.bookcopy.entity.BookCopy;
import org.springframework.stereotype.Component;

@Component
public class BookCopyMapper {

    public BookCopy toEntity(CreateBookCopyRequest request) {

        return BookCopy.builder()
                .barcode(request.getBarcode())
                .acquisitionNumber(request.getAcquisitionNumber())
                .shelfLocation(request.getShelfLocation())
                .conditionNotes(request.getConditionNotes())
                .build();
    }

    public void updateEntity(
            BookCopy bookCopy,
            UpdateBookCopyRequest request) {

        bookCopy.setAcquisitionNumber(
                request.getAcquisitionNumber()
        );

        // bookCopy.setStatus(
        //         request.getStatus()
        // );

        bookCopy.setShelfLocation(
                request.getShelfLocation()
        );

        bookCopy.setConditionNotes(
                request.getConditionNotes()
        );

        // bookCopy.setActive(
        //         request.isActive()
        // );
    }

    public BookCopyResponse toResponse(BookCopy bookCopy) {

        return BookCopyResponse.builder()
                .id(bookCopy.getId())
                .barcode(bookCopy.getBarcode())
                .acquisitionNumber(
                        bookCopy.getAcquisitionNumber()
                )
                .status(bookCopy.getStatus())
                .shelfLocation(
                        bookCopy.getShelfLocation()
                )
                .conditionNotes(
                        bookCopy.getConditionNotes()
                )
                .active(bookCopy.isActive())

                .bookId(
                        bookCopy.getBook().getId()
                )
                .bookTitle(
                        bookCopy.getBook().getTitle()
                )
                .isbn(
                        bookCopy.getBook().getIsbn()
                )

                .createdAt(
                        bookCopy.getCreatedAt()
                )
                .updatedAt(
                        bookCopy.getUpdatedAt()
                )
                .build();
    }
}