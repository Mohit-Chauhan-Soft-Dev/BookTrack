package com.booktrack.fine.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFineRequest {

    @NotNull(message = "Circulation is required")
    private Long circulationId;

    @NotNull(message = "Fine amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Fine amount must be greater than zero"
    )
    private BigDecimal amount;

    @NotBlank(message = "Fine reason is required")
    @Size(max = 500)
    private String reason;

    @Size(max = 1000)
    private String notes;
}