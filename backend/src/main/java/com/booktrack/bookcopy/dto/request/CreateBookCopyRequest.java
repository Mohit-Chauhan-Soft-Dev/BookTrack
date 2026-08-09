package com.booktrack.bookcopy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookCopyRequest {

    @NotBlank(message = "Barcode is required")
    @Size(max = 100)
    private String barcode;

    @Size(max = 100)
    private String acquisitionNumber;

    @Size(max = 100)
    private String shelfLocation;

    @Size(max = 1000)
    private String conditionNotes;

    @NotNull(message = "Book is required")
    private Long bookId;

}