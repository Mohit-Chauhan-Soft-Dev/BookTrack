package com.booktrack.circulation.dto.response;

import com.booktrack.circulation.enums.CirculationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CirculationResponse {

    private Long id;

    private Long bookCopyId;
    private String barcode;

    private Long bookId;
    private String bookTitle;
    private String isbn;

    private Long userId;
    private String userName;
    private String userEmail;

    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;

    private CirculationStatus status;

    private Integer renewalCount;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}