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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/circulations")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/borrow")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_SUPER_ADMIN',
                'ROLE_ADMIN',
                'ROLE_LIBRARIAN',
                'ROLE_ASSISTANT_LIBRARIAN'
            )
            """)
    public ResponseEntity<CirculationResponse> borrowBook(
            @Valid @RequestBody BorrowBookRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        circulationService.borrowBook(request));
    }

    @PostMapping("/return")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_SUPER_ADMIN',
                'ROLE_ADMIN',
                'ROLE_LIBRARIAN',
                'ROLE_ASSISTANT_LIBRARIAN'
            )
            """)
    public ResponseEntity<CirculationResponse> returnBook(
            @Valid @RequestBody ReturnBookRequest request) {

        return ResponseEntity.ok(
                circulationService.returnBook(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CirculationResponse> getCirculationById(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                circulationService.getCirculationById(
                        id,
                        authentication));
    }

    @GetMapping
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_SUPER_ADMIN',
                'ROLE_ADMIN',
                'ROLE_LIBRARIAN'
            )
            """)
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
                        sortDirection));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<CirculationResponse>> getCirculationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            Authentication authentication) {

        return ResponseEntity.ok(
                circulationService.getCirculationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        authentication));
    }

    @GetMapping("/book-copy/{bookCopyId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_SUPER_ADMIN',
                'ROLE_ADMIN',
                'ROLE_LIBRARIAN'
            )
            """)
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
                        sortDirection));
    }
}