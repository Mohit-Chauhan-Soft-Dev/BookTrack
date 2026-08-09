package com.booktrack.book.dto.response;

import com.booktrack.book.enums.BookStatus;
import com.booktrack.book.enums.Language;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;

    private String isbn;

    private String title;

    private String subtitle;

    private String description;

    private Language language;

    private String edition;

    private Integer publicationYear;

    private Integer totalPages;

    private Integer totalCopies;

    private Integer availableCopies;

    private String shelfLocation;

    private String coverImage;

    private BigDecimal price;

    private boolean active;

    private BookStatus status;

    private Long categoryId;

    private String categoryName;

    private Long publisherId;

    private String publisherName;

    private Set<AuthorSummary> authors;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}