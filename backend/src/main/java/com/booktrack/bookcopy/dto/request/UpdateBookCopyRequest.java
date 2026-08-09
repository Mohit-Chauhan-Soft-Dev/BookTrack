package com.booktrack.bookcopy.dto.request;

// import com.booktrack.bookcopy.enums.BookCopyStatus;
// import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookCopyRequest {

    @Size(max = 100)
    private String acquisitionNumber;

    // @NotNull(message = "Status is required")
    // private BookCopyStatus status;

    @Size(max = 100)
    private String shelfLocation;

    @Size(max = 1000)
    private String conditionNotes;

    // private boolean active;

}