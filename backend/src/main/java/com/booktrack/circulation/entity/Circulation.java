package com.booktrack.circulation.entity;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.common.entity.BaseEntity;
import com.booktrack.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "circulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Circulation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "book_copy_id",
            nullable = false
    )
    private BookCopy bookCopy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime dueAt;

    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CirculationStatus status = CirculationStatus.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private Integer renewalCount = 0;

    @Column(length = 1000)
    private String notes;
}