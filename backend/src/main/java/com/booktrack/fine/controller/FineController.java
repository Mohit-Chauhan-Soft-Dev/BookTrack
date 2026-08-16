package com.booktrack.fine.controller;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.fine.dto.request.CreateFineRequest;
import com.booktrack.fine.dto.request.PayFineRequest;
import com.booktrack.fine.dto.response.FineResponse;
import com.booktrack.fine.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/fines")
@RequiredArgsConstructor
public class FineController {

        private final FineService fineService;

        @PostMapping
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_SUPER_ADMIN',
                                'ROLE_ADMIN',
                                'ROLE_LIBRARIAN'
                        )
                        """)
        public ResponseEntity<FineResponse> createFine(
                        @Valid @RequestBody CreateFineRequest request) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                fineService.createFine(request));
        }

        @PostMapping("/pay")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<FineResponse> payFine(
                        @Valid @RequestBody PayFineRequest request,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                fineService.payFine(
                                                request,
                                                authentication));
        }

        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<FineResponse> getFineById(
                        @PathVariable Long id,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                fineService.getFineById(
                                                id,
                                                authentication));
        }

        @GetMapping("/circulation/{circulationId}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<FineResponse> getFineByCirculation(
                        @PathVariable Long circulationId,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                fineService.getFineByCirculation(
                                                circulationId,
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
        public ResponseEntity<PageResponse<FineResponse>> getAllFines(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                return ResponseEntity.ok(
                                fineService.getAllFines(
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @GetMapping("/user/{userId}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<PageResponse<FineResponse>> getFinesByUser(
                        @PathVariable Long userId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                fineService.getFinesByUser(
                                                userId,
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection,
                                                authentication));
        }
}