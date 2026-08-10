package com.booktrack.reservation.mapper;

import com.booktrack.reservation.dto.response.ReservationResponse;
import com.booktrack.reservation.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(
            Reservation reservation) {

        return ReservationResponse.builder()
                .id(reservation.getId())

                .bookId(
                        reservation.getBook().getId()
                )

                .bookTitle(
                        reservation.getBook().getTitle()
                )

                .isbn(
                        reservation.getBook().getIsbn()
                )

                .userId(
                        reservation.getUser().getId()
                )

                .userName(
                        reservation.getUser().getFirstName()
                                + " "
                                + reservation.getUser().getLastName()
                )

                .userEmail(
                        reservation.getUser().getEmail()
                )

                .status(reservation.getStatus())

                .reservedAt(
                        reservation.getReservedAt()
                )

                .fulfilledAt(
                        reservation.getFulfilledAt()
                )

                .completedAt(
                        reservation.getCompletedAt()
                )

                .cancelledAt(
                        reservation.getCancelledAt()
                )

                .expiresAt(
                        reservation.getExpiresAt()
                )

                .notes(
                        reservation.getNotes()
                )

                .createdAt(
                        reservation.getCreatedAt()
                )

                .updatedAt(
                        reservation.getUpdatedAt()
                )

                .build();
    }
}