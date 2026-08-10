package com.booktrack.reservation.repository;

import com.booktrack.reservation.entity.Reservation;
import com.booktrack.reservation.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReservationRepository extends
        JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {

    Page<Reservation> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<Reservation> findByBookId(
            Long bookId,
            Pageable pageable
    );

    Page<Reservation> findByStatus(
            ReservationStatus status,
            Pageable pageable
    );

    Page<Reservation> findByBookIdAndStatus(
            Long bookId,
            ReservationStatus status,
            Pageable pageable
    );

    Optional<Reservation> findFirstByBookIdAndStatusOrderByReservedAtAsc(
            Long bookId,
            ReservationStatus status
    );

    boolean existsByUserIdAndBookIdAndStatus(
            Long userId,
            Long bookId,
            ReservationStatus status
    );
}