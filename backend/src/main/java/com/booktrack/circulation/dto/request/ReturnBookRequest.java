package com.booktrack.circulation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnBookRequest {

    @NotNull(message = "Circulation is required")
    private Long circulationId;

    private String notes;
}