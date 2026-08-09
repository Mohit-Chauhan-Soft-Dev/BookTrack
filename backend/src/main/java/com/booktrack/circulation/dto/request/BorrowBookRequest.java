package com.booktrack.circulation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowBookRequest {

    @NotNull(message = "Book copy is required")
    private Long bookCopyId;

    @NotNull(message = "User is required")
    private Long userId;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueAt;

    private String notes;
}