package com.booktrack.book.dto.request;

import com.booktrack.book.enums.BookStatus;
import com.booktrack.book.enums.Language;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookRequest {

    @NotBlank(message = "ISBN is required")
    @Size(max = 20)
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String subtitle;

    @Size(max = 5000)
    private String description;

    @NotNull
    private Language language;

    @Size(max = 50)
    private String edition;

    @NotNull
    @Min(1000)
    @Max(9999)
    private Integer publicationYear;

    @NotNull
    @Positive
    private Integer totalPages;

    @Size(max = 100)
    private String shelfLocation;

    @Size(max = 500)
    private String coverImage;

    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    private BookStatus status;

    private boolean active;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long publisherId;

    @NotEmpty
    private Set<Long> authorIds;

}