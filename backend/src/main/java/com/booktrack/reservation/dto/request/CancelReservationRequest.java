package com.booktrack.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelReservationRequest {

    @NotNull(message = "Reservation is required")
    private Long reservationId;

    @Size(max = 1000)
    private String notes;
}