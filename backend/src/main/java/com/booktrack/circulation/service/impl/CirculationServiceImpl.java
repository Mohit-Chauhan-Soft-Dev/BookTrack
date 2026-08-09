package com.booktrack.circulation.service.impl;

import com.booktrack.book.entity.Book;
import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.bookcopy.repository.BookCopyRepository;
import com.booktrack.circulation.dto.request.BorrowBookRequest;
import com.booktrack.circulation.dto.request.ReturnBookRequest;
import com.booktrack.circulation.dto.response.CirculationResponse;
import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.circulation.mapper.CirculationMapper;
import com.booktrack.circulation.repository.CirculationRepository;
import com.booktrack.circulation.service.CirculationService;
import com.booktrack.circulation.validator.CirculationValidator;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;
import com.booktrack.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CirculationServiceImpl implements CirculationService {

        private final CirculationRepository circulationRepository;

        private final CirculationMapper circulationMapper;

        private final CirculationValidator circulationValidator;

        private final PageResponseMapper pageResponseMapper;

        private final BookCopyRepository bookCopyRepository;

        @Override
        public CirculationResponse borrowBook(
                        BorrowBookRequest request) {

                BookCopy bookCopy = circulationValidator.validateBookCopyExists(
                                request.getBookCopyId());

                User user = circulationValidator.validateUserExists(
                                request.getUserId());

                circulationValidator.validateBorrowableBookCopy(
                                bookCopy);

                if (!user.isEnabled()) {

                        throw new BadRequestException(
                                        "User account is disabled.");
                }

                if (!user.isAccountNonLocked()) {

                        throw new BadRequestException(
                                        "User account is locked.");
                }

                LocalDateTime issuedAt = LocalDateTime.now();

                if (!request.getDueAt().isAfter(issuedAt)) {

                        throw new BadRequestException(
                                        "Due date must be in the future.");
                }

                Circulation circulation = Circulation.builder()
                                .bookCopy(bookCopy)
                                .user(user)
                                .issuedAt(issuedAt)
                                .dueAt(request.getDueAt())
                                .status(CirculationStatus.ACTIVE)
                                .renewalCount(0)
                                .notes(request.getNotes())
                                .build();

                bookCopy.setStatus(
                                BookCopyStatus.BORROWED);

                Book book = bookCopy.getBook();

                if (book.getAvailableCopies() <= 0) {
                        throw new BadRequestException(
                                        "No available copies for this book.");
                }

                book.setAvailableCopies(
                                book.getAvailableCopies() - 1);

                bookCopyRepository.save(bookCopy);

                Circulation savedCirculation = circulationRepository.save(circulation);

                return circulationMapper.toResponse(
                                savedCirculation);
        }

        @Override
        public CirculationResponse returnBook(
                        ReturnBookRequest request) {

                Circulation circulation = circulationValidator.validateCirculationExists(
                                request.getCirculationId());

                circulationValidator.validateReturnableCirculation(
                                circulation);

                BookCopy bookCopy = circulation.getBookCopy();

                Book book = bookCopy.getBook();

                LocalDateTime returnedAt = LocalDateTime.now();

                circulation.setReturnedAt(returnedAt);

                circulation.setStatus(
                                CirculationStatus.RETURNED);

                if (request.getNotes() != null
                                && !request.getNotes().isBlank()) {

                        circulation.setNotes(
                                        request.getNotes());
                }

                bookCopy.setStatus(
                                BookCopyStatus.AVAILABLE);

                book.setAvailableCopies(
                                book.getAvailableCopies() + 1);

                bookCopyRepository.save(bookCopy);

                Circulation updatedCirculation = circulationRepository.save(circulation);

                return circulationMapper.toResponse(
                                updatedCirculation);
        }

        @Override
        @Transactional(readOnly = true)
        public CirculationResponse getCirculationById(
                        Long id) {

                Circulation circulation = circulationValidator.validateCirculationExists(
                                id);

                return circulationMapper.toResponse(
                                circulation);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<CirculationResponse> getAllCirculations(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Circulation> circulationPage = circulationRepository.findAll(
                                pageable);

                return pageResponseMapper.toPageResponse(
                                circulationPage,
                                circulationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<CirculationResponse> getCirculationsByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                circulationValidator.validateUserExists(
                                userId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Circulation> circulationPage = circulationRepository.findByUserId(
                                userId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                circulationPage,
                                circulationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<CirculationResponse> getCirculationsByBookCopy(
                        Long bookCopyId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                circulationValidator.validateBookCopyExists(
                                bookCopyId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Circulation> circulationPage = circulationRepository.findByBookCopyId(
                                bookCopyId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                circulationPage,
                                circulationMapper::toResponse);
        }
}