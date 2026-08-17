package com.booktrack.book.controller;

import com.booktrack.book.dto.request.CreateBookRequest;
import com.booktrack.book.dto.request.UpdateBookRequest;
import com.booktrack.book.dto.response.BookResponse;
import com.booktrack.book.service.BookService;
import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(ApplicationConstants.BOOKS)
@RequiredArgsConstructor
public class BookController {

        private final BookService bookService;

        @PostMapping
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_LIBRARIAN',
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<BookResponse> createBook(
                        @Valid @RequestBody CreateBookRequest request) {

                BookResponse response = bookService.createBook(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<BookResponse> getBookById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                bookService.getBookById(id));
        }

        @GetMapping
        public ResponseEntity<PageResponse<BookResponse>> getAllBooks(

                        @RequestParam(defaultValue = "0") int page,

                        @RequestParam(defaultValue = "10") int size,

                        @RequestParam(defaultValue = "title") String sortBy,

                        @RequestParam(defaultValue = "asc") String sortDirection) {

                return ResponseEntity.ok(
                                bookService.getAllBooks(
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @PutMapping("/{id}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_LIBRARIAN',
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<BookResponse> updateBook(

                        @PathVariable Long id,

                        @Valid @RequestBody UpdateBookRequest request) {

                return ResponseEntity.ok(
                                bookService.updateBook(id, request));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<Void> deleteBook(
                        @PathVariable Long id) {

                bookService.deleteBook(id);

                return ResponseEntity.noContent().build();
        }
}