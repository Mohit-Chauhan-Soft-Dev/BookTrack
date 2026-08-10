package com.booktrack.reservation.dto.response;

import com.booktrack.reservation.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;

    private Long bookId;
    private String bookTitle;
    private String isbn;

    private Long userId;
    private String userName;
    private String userEmail;

    private ReservationStatus status;

    private LocalDateTime reservedAt;

    private LocalDateTime fulfilledAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime expiresAt;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}