package com.booktrack.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FulfillReservationRequest {

    @NotNull(message = "Reservation is required")
    private Long reservationId;

    @NotNull(message = "Book copy is required")
    private Long bookCopyId;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueAt;

    @Size(max = 1000)
    private String notes;
}