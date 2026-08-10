package com.booktrack.reservation.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.reservation.dto.request.CancelReservationRequest;
import com.booktrack.reservation.dto.request.CreateReservationRequest;
import com.booktrack.reservation.dto.response.ReservationResponse;
import com.booktrack.reservation.dto.request.FulfillReservationRequest;
import com.booktrack.reservation.dto.request.CompleteReservationRequest;

public interface ReservationService {

        ReservationResponse createReservation(
                        CreateReservationRequest request);

        ReservationResponse cancelReservation(
                        CancelReservationRequest request);

        ReservationResponse getReservationById(
                        Long id);

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
                        String sortDirection);

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