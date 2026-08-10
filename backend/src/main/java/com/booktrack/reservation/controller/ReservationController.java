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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.RESERVATIONS)
@RequiredArgsConstructor
public class ReservationController {

        private final ReservationService reservationService;

        @PostMapping
        public ResponseEntity<ReservationResponse> createReservation(
                        @Valid @RequestBody CreateReservationRequest request) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                reservationService.createReservation(request));
        }

        @PostMapping("/cancel")
        public ResponseEntity<ReservationResponse> cancelReservation(
                        @Valid @RequestBody CancelReservationRequest request) {

                return ResponseEntity.ok(
                                reservationService.cancelReservation(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ReservationResponse> getReservationById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                reservationService.getReservationById(id));
        }

        @GetMapping
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
        public ResponseEntity<PageResponse<ReservationResponse>> getReservationsByUser(
                        @PathVariable Long userId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                return ResponseEntity.ok(
                                reservationService.getReservationsByUser(
                                                userId,
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @GetMapping("/book/{bookId}")
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
        public ResponseEntity<ReservationResponse> fulfillReservation(
                        @Valid @RequestBody FulfillReservationRequest request) {

                return ResponseEntity.ok(
                                reservationService.fulfillReservation(request));
        }

        @PostMapping("/complete")
        public ResponseEntity<ReservationResponse> completeReservation(
                        @Valid @RequestBody CompleteReservationRequest request) {

                return ResponseEntity.ok(
                                reservationService.completeReservation(request));
        }
}