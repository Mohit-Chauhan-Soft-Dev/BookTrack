package com.booktrack.bookcopy.service.impl;

import com.booktrack.book.entity.Book;
import com.booktrack.book.validator.BookValidator;
import com.booktrack.bookcopy.dto.request.CreateBookCopyRequest;
import com.booktrack.bookcopy.dto.request.UpdateBookCopyRequest;
import com.booktrack.bookcopy.dto.response.BookCopyResponse;
import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.mapper.BookCopyMapper;
import com.booktrack.bookcopy.repository.BookCopyRepository;
import com.booktrack.bookcopy.service.BookCopyService;
import com.booktrack.bookcopy.validator.BookCopyValidator;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booktrack.bookcopy.enums.BookCopyStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCopyServiceImpl implements BookCopyService {

        private final BookCopyRepository bookCopyRepository;

        private final BookCopyMapper bookCopyMapper;

        private final BookCopyValidator bookCopyValidator;

        private final BookValidator bookValidator;

        private final PageResponseMapper pageResponseMapper;

        @Override
        public BookCopyResponse createBookCopy(
                        CreateBookCopyRequest request) {

                String barcode = request.getBarcode().trim();

                bookCopyValidator.validateDuplicateBarcode(barcode);

                Book book = bookValidator.validateBookExists(
                                request.getBookId());

                if (!book.isActive()) {

                        throw new BadRequestException(
                                        "Cannot create a copy for an inactive book.");
                }

                request.setBarcode(barcode);

                BookCopy bookCopy = bookCopyMapper.toEntity(request);

                bookCopy.setBook(book);

                book.setTotalCopies(book.getTotalCopies() + 1);
                book.setAvailableCopies(book.getAvailableCopies() + 1);

                BookCopy savedBookCopy = bookCopyRepository.save(bookCopy);

                return bookCopyMapper.toResponse(savedBookCopy);
        }

        @Override
        public BookCopyResponse updateBookCopy(
                        Long id,
                        UpdateBookCopyRequest request) {

                BookCopy bookCopy = bookCopyValidator.validateBookCopyExists(id);

                bookCopyMapper.updateEntity(
                                bookCopy,
                                request);

                BookCopy updatedBookCopy = bookCopyRepository.save(bookCopy);

                return bookCopyMapper.toResponse(updatedBookCopy);
        }

        @Override
        public void deleteBookCopy(Long id) {

                BookCopy bookCopy = bookCopyValidator.validateBookCopyExists(id);

                if (!bookCopy.isActive()) {

                        throw new BadRequestException(
                                        "Book copy is already inactive.");
                }

                if (bookCopy.getStatus() == BookCopyStatus.BORROWED) {

                        throw new BadRequestException(
                                        "Borrowed book copy cannot be deleted.");
                }

                Book book = bookCopy.getBook();

                book.setTotalCopies(
                                book.getTotalCopies() - 1);

                if (bookCopy.getStatus() == BookCopyStatus.AVAILABLE) {

                        book.setAvailableCopies(
                                        book.getAvailableCopies() - 1);
                }

                bookCopy.setActive(false);

                bookCopyRepository.save(bookCopy);
        }

        @Override
        @Transactional(readOnly = true)
        public BookCopyResponse getBookCopyById(Long id) {

                BookCopy bookCopy = bookCopyValidator.validateBookCopyExists(id);

                return bookCopyMapper.toResponse(bookCopy);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<BookCopyResponse> getAllBookCopies(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<BookCopy> bookCopyPage = bookCopyRepository.findAll(pageable);

                return pageResponseMapper.toPageResponse(
                                bookCopyPage,
                                bookCopyMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<BookCopyResponse> getBookCopiesByBook(
                        Long bookId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                bookValidator.validateBookExists(bookId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<BookCopy> bookCopyPage = bookCopyRepository.findByBookId(
                                bookId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                bookCopyPage,
                                bookCopyMapper::toResponse);
        }
}