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

@RestController
@RequestMapping("/api/v1/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping
    public ResponseEntity<FineResponse> createFine(
            @Valid @RequestBody CreateFineRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        fineService.createFine(request));
    }

    @PostMapping("/pay")
    public ResponseEntity<FineResponse> payFine(
            @Valid @RequestBody PayFineRequest request) {

        return ResponseEntity.ok(
                fineService.payFine(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getFineById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                fineService.getFineById(id));
    }

    @GetMapping("/circulation/{circulationId}")
    public ResponseEntity<FineResponse> getFineByCirculation(
            @PathVariable Long circulationId) {

        return ResponseEntity.ok(
                fineService.getFineByCirculation(
                        circulationId));
    }

    @GetMapping
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
    public ResponseEntity<PageResponse<FineResponse>> getFinesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                fineService.getFinesByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }
}