package com.booktrack.bookcopy.entity;

import com.booktrack.book.entity.Book;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "book_copies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_book_copy_barcode",
                        columnNames = "barcode"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopy extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String barcode;

    @Column(length = 100)
    private String acquisitionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private BookCopyStatus status = BookCopyStatus.AVAILABLE;

    @Column(length = 100)
    private String shelfLocation;

    @Column(length = 1000)
    private String conditionNotes;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "book_id",
            nullable = false
    )
    private Book book;

}