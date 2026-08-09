package com.booktrack.book.validator;

import com.booktrack.book.entity.Book;
import com.booktrack.book.repository.BookRepository;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private final BookRepository bookRepository;

    public Book validateBookExists(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        ));
    }

    public void validateDuplicateIsbn(String isbn) {

        String normalizedIsbn = isbn.trim();

        if (bookRepository.existsByIsbn(normalizedIsbn)) {

            throw new DuplicateResourceException(
                    "Book already exists with ISBN: " + normalizedIsbn
            );
        }
    }

    public void validateDuplicateIsbnForUpdate(
            Long bookId,
            String isbn) {

        String normalizedIsbn = isbn.trim();

        bookRepository.findByIsbn(normalizedIsbn)
                .ifPresent(existingBook -> {

                    if (!existingBook.getId().equals(bookId)) {

                        throw new DuplicateResourceException(
                                "Book already exists with ISBN: "
                                        + normalizedIsbn
                        );
                    }
                });
    }

    // public void validateAvailableCopies(
    //         Integer totalCopies,
    //         Integer availableCopies) {

    //     if (availableCopies > totalCopies) {

    //         throw new BadRequestException(
    //                 "Available copies cannot be greater than total copies."
    //         );
    //     }
    // }

}