package com.booktrack.reservation.validator;

import com.booktrack.book.entity.Book;
import com.booktrack.book.repository.BookRepository;
import com.booktrack.exception.BadRequestException;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.reservation.entity.Reservation;
import com.booktrack.reservation.enums.ReservationStatus;
import com.booktrack.reservation.repository.ReservationRepository;
import com.booktrack.user.entity.User;
import com.booktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.bookcopy.repository.BookCopyRepository;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

        private final ReservationRepository reservationRepository;

        private final BookRepository bookRepository;

        private final UserRepository userRepository;

        private final BookCopyRepository bookCopyRepository;

        public Reservation validateReservationExists(Long id) {

                return reservationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Reservation not found with id: " + id));
        }

        public Book validateBookExists(Long bookId) {

                return bookRepository.findById(bookId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Book not found with id: " + bookId));
        }

        public User validateUserExists(Long userId) {

                return userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with id: " + userId));
        }

        public void validateDuplicateReservation(
                        Long userId,
                        Long bookId) {

                if (reservationRepository
                                .existsByUserIdAndBookIdAndStatus(
                                                userId,
                                                bookId,
                                                ReservationStatus.PENDING)) {

                        throw new DuplicateResourceException(
                                        "User already has a pending reservation for this book.");
                }
        }

        public void validateCancellableReservation(
                        Reservation reservation) {

                if (reservation.getStatus() != ReservationStatus.PENDING) {

                        throw new BadRequestException(
                                        "Only pending reservations can be cancelled.");
                }
        }

        public BookCopy validateFulfillableBookCopy(
                        Reservation reservation,
                        Long bookCopyId) {

                BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Book copy not found with id: "
                                                                + bookCopyId));

                if (!bookCopy.getBook().getId()
                                .equals(reservation.getBook().getId())) {

                        throw new BadRequestException(
                                        "Book copy does not belong to the reserved book.");
                }

                if (!bookCopy.isActive()) {

                        throw new BadRequestException(
                                        "Cannot fulfill reservation with an inactive book copy.");
                }

                if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {

                        throw new BadRequestException(
                                        "Book copy is not available for fulfillment.");
                }

                return bookCopy;
        }

        public void validateFulfillableReservation(
                        Reservation reservation) {

                if (reservation.getStatus() != ReservationStatus.PENDING) {

                        throw new BadRequestException(
                                        "Only pending reservations can be fulfilled.");
                }

                if (reservation.getExpiresAt() != null
                                && reservation.getExpiresAt()
                                                .isBefore(java.time.LocalDateTime.now())) {

                        throw new BadRequestException(
                                        "Reservation has expired.");
                }
        }

        public void validateCompletableReservation(
                        Reservation reservation) {

                if (reservation.getStatus() != ReservationStatus.FULFILLED) {

                        throw new BadRequestException(
                                        "Only fulfilled reservations can be completed.");
                }
        }
}