package com.booktrack.reservation.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.reservation.dto.request.CancelReservationRequest;
import com.booktrack.reservation.dto.request.CreateReservationRequest;
import com.booktrack.reservation.dto.response.ReservationResponse;
import com.booktrack.reservation.dto.request.FulfillReservationRequest;
import com.booktrack.reservation.dto.request.CompleteReservationRequest;
import org.springframework.security.core.Authentication;

public interface ReservationService {

        ReservationResponse createReservation(
                        CreateReservationRequest request,
                        Authentication authentication);

        ReservationResponse cancelReservation(
                        CancelReservationRequest request,
                        Authentication authentication);

        ReservationResponse getReservationById(
                        Long id,
                        Authentication authentication);

        PageResponse<ReservationResponse> getAllReservations(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection);

        PageResponse<ReservationResponse> getReservationsByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection,
                        Authentication authentication);

        PageResponse<ReservationResponse> getReservationsByBook(
                        Long bookId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection);

        ReservationResponse fulfillReservation(
                        FulfillReservationRequest request);

        ReservationResponse completeReservation(
                        CompleteReservationRequest request);
}