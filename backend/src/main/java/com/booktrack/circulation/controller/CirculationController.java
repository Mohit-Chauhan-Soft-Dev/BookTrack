package com.booktrack.circulation.controller;

import com.booktrack.circulation.dto.request.BorrowBookRequest;
import com.booktrack.circulation.dto.request.ReturnBookRequest;
import com.booktrack.circulation.dto.response.CirculationResponse;
import com.booktrack.circulation.service.CirculationService;
import com.booktrack.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/circulations")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/borrow")
    public ResponseEntity<CirculationResponse> borrowBook(
            @Valid @RequestBody BorrowBookRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        circulationService.borrowBook(request)
                );
    }

    @PostMapping("/return")
    public ResponseEntity<CirculationResponse> returnBook(
            @Valid @RequestBody ReturnBookRequest request) {

        return ResponseEntity.ok(
                circulationService.returnBook(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CirculationResponse> getCirculationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                circulationService.getCirculationById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<CirculationResponse>> getAllCirculations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                circulationService.getAllCirculations(
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<CirculationResponse>> getCirculationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                circulationService.getCirculationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/book-copy/{bookCopyId}")
    public ResponseEntity<PageResponse<CirculationResponse>> getCirculationsByBookCopy(
            @PathVariable Long bookCopyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                circulationService.getCirculationsByBookCopy(
                        bookCopyId,
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }
}