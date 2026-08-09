package com.booktrack.bookcopy.dto.response;

import com.booktrack.bookcopy.enums.BookCopyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyResponse {

    private Long id;

    private String barcode;

    private String acquisitionNumber;

    private BookCopyStatus status;

    private String shelfLocation;

    private String conditionNotes;

    private boolean active;

    private Long bookId;

    private String bookTitle;

    private String isbn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}