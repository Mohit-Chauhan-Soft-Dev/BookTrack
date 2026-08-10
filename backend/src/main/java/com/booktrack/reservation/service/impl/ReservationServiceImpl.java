package com.booktrack.reservation.service.impl;

import com.booktrack.book.entity.Book;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.reservation.dto.request.CancelReservationRequest;
import com.booktrack.reservation.dto.request.CreateReservationRequest;
import com.booktrack.reservation.dto.response.ReservationResponse;
import com.booktrack.reservation.entity.Reservation;
import com.booktrack.reservation.enums.ReservationStatus;
import com.booktrack.reservation.mapper.ReservationMapper;
import com.booktrack.reservation.repository.ReservationRepository;
import com.booktrack.reservation.service.ReservationService;
import com.booktrack.reservation.validator.ReservationValidator;
import com.booktrack.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.circulation.dto.request.BorrowBookRequest;
import com.booktrack.circulation.service.CirculationService;
import com.booktrack.reservation.dto.request.FulfillReservationRequest;
import com.booktrack.reservation.dto.request.CompleteReservationRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

        private final ReservationRepository reservationRepository;

        private final ReservationMapper reservationMapper;

        private final ReservationValidator reservationValidator;

        private final PageResponseMapper pageResponseMapper;

        private final CirculationService circulationService;

        @Override
        public ReservationResponse createReservation(
                        CreateReservationRequest request) {

                /*
                 * Temporary user resolution:
                 *
                 * Until we wire the authenticated user into the
                 * reservation flow, use User #1 for our initial
                 * development/testing flow.
                 *
                 * We will replace this with the authenticated user
                 * once the reservation module's core flow is verified.
                 */
                Long userId = 1L;

                User user = reservationValidator.validateUserExists(
                                userId);

                Book book = reservationValidator.validateBookExists(
                                request.getBookId());

                reservationValidator.validateDuplicateReservation(
                                userId,
                                book.getId());

                LocalDateTime now = LocalDateTime.now();

                Reservation reservation = Reservation.builder()
                                .user(user)
                                .book(book)
                                .status(ReservationStatus.PENDING)
                                .reservedAt(now)
                                .expiresAt(now.plusDays(3))
                                .notes(request.getNotes())
                                .build();

                Reservation savedReservation = reservationRepository.save(
                                reservation);

                return reservationMapper.toResponse(
                                savedReservation);
        }

        @Override
        public ReservationResponse cancelReservation(
                        CancelReservationRequest request) {

                Reservation reservation = reservationValidator.validateReservationExists(
                                request.getReservationId());

                reservationValidator.validateCancellableReservation(
                                reservation);

                reservation.setStatus(
                                ReservationStatus.CANCELLED);

                reservation.setCancelledAt(
                                LocalDateTime.now());

                if (request.getNotes() != null
                                && !request.getNotes().isBlank()) {

                        reservation.setNotes(
                                        request.getNotes());
                }

                Reservation updatedReservation = reservationRepository.save(
                                reservation);

                return reservationMapper.toResponse(
                                updatedReservation);
        }

        @Override
        public ReservationResponse fulfillReservation(
                        FulfillReservationRequest request) {

                Reservation reservation = reservationValidator.validateReservationExists(
                                request.getReservationId());

                reservationValidator.validateFulfillableReservation(
                                reservation);

                BookCopy bookCopy = reservationValidator.validateFulfillableBookCopy(
                                reservation,
                                request.getBookCopyId());

                BorrowBookRequest borrowRequest = BorrowBookRequest.builder()
                                .bookCopyId(bookCopy.getId())
                                .userId(reservation.getUser().getId())
                                .dueAt(request.getDueAt())
                                .notes(request.getNotes())
                                .build();

                circulationService.borrowBook(
                                borrowRequest);

                reservation.setStatus(
                                ReservationStatus.FULFILLED);

                reservation.setFulfilledAt(
                                LocalDateTime.now());

                if (request.getNotes() != null
                                && !request.getNotes().isBlank()) {

                        reservation.setNotes(
                                        request.getNotes());
                }

                Reservation updatedReservation = reservationRepository.save(
                                reservation);

                return reservationMapper.toResponse(
                                updatedReservation);
        }

        @Override
        public ReservationResponse completeReservation(
                        CompleteReservationRequest request) {

                Reservation reservation = reservationValidator.validateReservationExists(
                                request.getReservationId());

                reservationValidator.validateCompletableReservation(
                                reservation);

                reservation.setStatus(
                                ReservationStatus.COMPLETED);

                reservation.setCompletedAt(
                                LocalDateTime.now());

                if (request.getNotes() != null
                                && !request.getNotes().isBlank()) {

                        reservation.setNotes(
                                        request.getNotes());
                }

                Reservation updatedReservation = reservationRepository.save(
                                reservation);

                return reservationMapper.toResponse(
                                updatedReservation);
        }

        @Override
        @Transactional(readOnly = true)
        public ReservationResponse getReservationById(
                        Long id) {

                Reservation reservation = reservationValidator.validateReservationExists(
                                id);

                return reservationMapper.toResponse(
                                reservation);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<ReservationResponse> getAllReservations(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Reservation> reservationPage = reservationRepository.findAll(
                                pageable);

                return pageResponseMapper.toPageResponse(
                                reservationPage,
                                reservationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<ReservationResponse> getReservationsByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                reservationValidator.validateUserExists(
                                userId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Reservation> reservationPage = reservationRepository.findByUserId(
                                userId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                reservationPage,
                                reservationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<ReservationResponse> getReservationsByBook(
                        Long bookId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                reservationValidator.validateBookExists(
                                bookId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Reservation> reservationPage = reservationRepository.findByBookId(
                                bookId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                reservationPage,
                                reservationMapper::toResponse);
        }
}