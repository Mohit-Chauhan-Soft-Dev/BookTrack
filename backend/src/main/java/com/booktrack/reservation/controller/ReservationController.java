package com.booktrack.reservation.controller;

import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.reservation.dto.request.CancelReservationRequest;
import com.booktrack.reservation.dto.request.CreateReservationRequest;
import com.booktrack.reservation.dto.request.FulfillReservationRequest;
import com.booktrack.reservation.dto.response.ReservationResponse;
import com.booktrack.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.booktrack.reservation.dto.request.CompleteReservationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.RESERVATIONS)
@RequiredArgsConstructor
public class ReservationController {

        private final ReservationService reservationService;

        @PostMapping
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<ReservationResponse> createReservation(
                        @Valid @RequestBody CreateReservationRequest request,
                        Authentication authentication) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                reservationService.createReservation(
                                                                request,
                                                                authentication));
        }

        @PostMapping("/cancel")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<ReservationResponse> cancelReservation(
                        @Valid @RequestBody CancelReservationRequest request,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                reservationService.cancelReservation(
                                                request,
                                                authentication));
        }

        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<ReservationResponse> getReservationById(
                        @PathVariable Long id,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                reservationService.getReservationById(
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
        public ResponseEntity<PageResponse<ReservationResponse>> getAllReservations(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                return ResponseEntity.ok(
                                reservationService.getAllReservations(
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @GetMapping("/user/{userId}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<PageResponse<ReservationResponse>> getReservationsByUser(
                        @PathVariable Long userId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                reservationService.getReservationsByUser(
                                                userId,
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection,
                                                authentication));
        }

        @GetMapping("/book/{bookId}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_SUPER_ADMIN',
                                'ROLE_ADMIN',
                                'ROLE_LIBRARIAN'
                        )
                        """)
        public ResponseEntity<PageResponse<ReservationResponse>> getReservationsByBook(
                        @PathVariable Long bookId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "reservedAt") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDirection) {

                return ResponseEntity.ok(
                                reservationService.getReservationsByBook(
                                                bookId,
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @PostMapping("/fulfill")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_SUPER_ADMIN',
                                'ROLE_ADMIN',
                                'ROLE_LIBRARIAN'
                        )
                        """)
        public ResponseEntity<ReservationResponse> fulfillReservation(
                        @Valid @RequestBody FulfillReservationRequest request) {

                return ResponseEntity.ok(
                                reservationService.fulfillReservation(request));
        }

        @PostMapping("/complete")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_SUPER_ADMIN',
                                'ROLE_ADMIN',
                                'ROLE_LIBRARIAN'
                        )
                        """)
        public ResponseEntity<ReservationResponse> completeReservation(
                        @Valid @RequestBody CompleteReservationRequest request) {

                return ResponseEntity.ok(
                                reservationService.completeReservation(request));
        }
}