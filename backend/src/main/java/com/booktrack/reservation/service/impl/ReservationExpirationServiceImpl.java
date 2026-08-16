package com.booktrack.reservation.service.impl;

import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.enums.NotificationType;
import com.booktrack.notification.service.NotificationService;
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

        private final NotificationService notificationService;

        @Override
        @Transactional
        public void markExpiredReservations() {

                LocalDateTime now = LocalDateTime.now();

                List<Reservation> expiredReservations =
                                reservationRepository.findByStatusAndExpiresAtBefore(
                                                ReservationStatus.PENDING,
                                                now);

                for (Reservation reservation : expiredReservations) {

                        reservation.setStatus(
                                        ReservationStatus.EXPIRED);

                        notificationService.createNotification(
                                        CreateNotificationRequest.builder()
                                                        .userId(reservation.getUser().getId())
                                                        .type(NotificationType.RESERVATION_EXPIRED)
                                                        .title("Reservation Expired")
                                                        .message(
                                                                        "Your reservation for "
                                                                                        + reservation.getBook()
                                                                                                        .getTitle()
                                                                                        + " has expired.")
                                                        .build());
                }

                if (!expiredReservations.isEmpty()) {

                        reservationRepository.saveAll(
                                        expiredReservations);
                }
        }
}