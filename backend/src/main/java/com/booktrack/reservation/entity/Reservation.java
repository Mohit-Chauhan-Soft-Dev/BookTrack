package com.booktrack.reservation.entity;

import com.booktrack.book.entity.Book;
import com.booktrack.common.entity.BaseEntity;
import com.booktrack.reservation.enums.ReservationStatus;
import com.booktrack.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reservations",
        indexes = {
                @Index(
                        name = "idx_reservation_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_reservation_book",
                        columnList = "book_id"
                ),
                @Index(
                        name = "idx_reservation_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "book_id",
            nullable = false
    )
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    @Builder.Default
    private ReservationStatus status =
            ReservationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime reservedAt;

    private LocalDateTime fulfilledAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime expiresAt;

    @Column(length = 1000)
    private String notes;
}