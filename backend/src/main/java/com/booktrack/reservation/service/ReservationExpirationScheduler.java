package com.booktrack.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExpirationScheduler {

    private final ReservationExpirationService reservationExpirationService;

    @Scheduled(fixedDelay = 60000)
    public void markExpiredReservations() {

        reservationExpirationService.markExpiredReservations();
    }
}