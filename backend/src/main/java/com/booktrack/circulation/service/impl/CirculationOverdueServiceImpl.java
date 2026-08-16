package com.booktrack.circulation.service.impl;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.circulation.repository.CirculationRepository;
import com.booktrack.circulation.service.CirculationOverdueService;
import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.enums.NotificationType;
import com.booktrack.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CirculationOverdueServiceImpl
                implements CirculationOverdueService {

        private final CirculationRepository circulationRepository;

        private final NotificationService notificationService;

        @Override
        @Transactional
        public void markOverdueCirculations() {

                LocalDateTime now = LocalDateTime.now();

                List<Circulation> overdueCirculations = circulationRepository.findByStatusAndDueAtBefore(
                                CirculationStatus.ACTIVE,
                                now);

                for (Circulation circulation : overdueCirculations) {

                        circulation.setStatus(
                                        CirculationStatus.OVERDUE);

                        notificationService.createNotification(
                                        CreateNotificationRequest.builder()
                                                        .userId(
                                                                        circulation.getUser().getId())
                                                        .type(
                                                                        NotificationType.CIRCULATION_OVERDUE)
                                                        .title(
                                                                        "Book Overdue")
                                                        .message(
                                                                        circulation.getBookCopy()
                                                                                        .getBook()
                                                                                        .getTitle()
                                                                                        + " is overdue. Please return it as soon as possible.")
                                                        .build());
                }

                if (!overdueCirculations.isEmpty()) {

                        circulationRepository.saveAll(
                                        overdueCirculations);
                }
        }
}