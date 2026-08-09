package com.booktrack.bookcopy.validator;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.repository.BookCopyRepository;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookCopyValidator {

    private final BookCopyRepository bookCopyRepository;

    public BookCopy validateBookCopyExists(Long id) {

        return bookCopyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book copy not found with id: " + id
                        )
                );
    }

    public void validateDuplicateBarcode(String barcode) {

        String normalizedBarcode = barcode.trim();

        if (bookCopyRepository.existsByBarcode(normalizedBarcode)) {

            throw new DuplicateResourceException(
                    "Book copy already exists with barcode: "
                            + normalizedBarcode
            );
        }
    }
}