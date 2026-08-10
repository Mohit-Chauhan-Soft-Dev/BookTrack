package com.booktrack.fine.dto.response;

import com.booktrack.fine.enums.FineStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineResponse {

    private Long id;

    private Long circulationId;

    private Long bookCopyId;
    private String barcode;

    private Long bookId;
    private String bookTitle;
    private String isbn;

    private Long userId;
    private String userName;
    private String userEmail;

    private BigDecimal amount;

    private String reason;

    private FineStatus status;

    private LocalDateTime paidAt;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}