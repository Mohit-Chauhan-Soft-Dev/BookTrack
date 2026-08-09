package com.booktrack.bookcopy.controller;

import com.booktrack.bookcopy.dto.request.CreateBookCopyRequest;
import com.booktrack.bookcopy.dto.request.UpdateBookCopyRequest;
import com.booktrack.bookcopy.dto.response.BookCopyResponse;
import com.booktrack.bookcopy.service.BookCopyService;
import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.BOOK_COPIES)
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @PostMapping
    public ResponseEntity<BookCopyResponse> createBookCopy(
            @Valid @RequestBody CreateBookCopyRequest request) {

        BookCopyResponse response =
                bookCopyService.createBookCopy(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponse> getBookCopyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookCopyService.getBookCopyById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookCopyResponse>>
    getAllBookCopies(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDirection) {

        return ResponseEntity.ok(
                bookCopyService.getAllBookCopies(
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<BookCopyResponse>>
    getBookCopiesByBook(

            @PathVariable Long bookId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDirection) {

        return ResponseEntity.ok(
                bookCopyService.getBookCopiesByBook(
                        bookId,
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookCopyResponse> updateBookCopy(

            @PathVariable Long id,

            @Valid
            @RequestBody UpdateBookCopyRequest request) {

        return ResponseEntity.ok(
                bookCopyService.updateBookCopy(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(
            @PathVariable Long id) {

        bookCopyService.deleteBookCopy(id);

        return ResponseEntity.noContent().build();
    }
}