package com.booktrack.reservation.service.impl;

import com.booktrack.reservation.entity.Reservation;
import com.booktrack.reservation.enums.ReservationStatus;
import com.booktrack.reservation.repository.ReservationRepository;
import com.booktrack.reservation.service.ReservationExpirationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationExpirationServiceImpl
        implements ReservationExpirationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public void markExpiredReservations() {

        LocalDateTime now = LocalDateTime.now();

        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndExpiresAtBefore(
                        ReservationStatus.PENDING,
                        now
                );

        for (Reservation reservation : expiredReservations) {

            reservation.setStatus(
                    ReservationStatus.EXPIRED
            );
        }

        if (!expiredReservations.isEmpty()) {

            reservationRepository.saveAll(
                    expiredReservations
            );
        }
    }
}