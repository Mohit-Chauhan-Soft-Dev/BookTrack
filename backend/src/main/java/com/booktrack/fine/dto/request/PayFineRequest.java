package com.booktrack.fine.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayFineRequest {

    @NotNull(message = "Fine is required")
    private Long fineId;

    @Size(max = 1000)
    private String notes;
}