package com.booktrack.book.service.impl;

import com.booktrack.author.entity.Author;
import com.booktrack.author.validator.AuthorValidator;
import com.booktrack.book.dto.request.CreateBookRequest;
import com.booktrack.book.dto.request.UpdateBookRequest;
import com.booktrack.book.dto.response.BookResponse;
import com.booktrack.book.entity.Book;
import com.booktrack.book.mapper.BookMapper;
import com.booktrack.book.repository.BookRepository;
import com.booktrack.book.service.BookService;
import com.booktrack.book.validator.BookValidator;
import com.booktrack.category.entity.Category;
import com.booktrack.category.validator.CategoryValidator;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;
import com.booktrack.publisher.entity.Publisher;
import com.booktrack.publisher.validator.PublisherValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookValidator bookValidator;

    private final CategoryValidator categoryValidator;

    private final PublisherValidator publisherValidator;

    private final AuthorValidator authorValidator;

    private final PageResponseMapper pageResponseMapper;

    @Override
    public BookResponse createBook(
            CreateBookRequest request) {

        String isbn = request.getIsbn().trim();

        bookValidator.validateDuplicateIsbn(isbn);

        request.setIsbn(isbn);

        Book book = bookMapper.toEntity(request);

        Category category = categoryValidator.validateCategoryExists(
                request.getCategoryId());

        Publisher publisher = publisherValidator.validatePublisherExists(
                request.getPublisherId());

        Set<Author> authors = request.getAuthorIds()
                .stream()
                .map(authorValidator::validateAuthorExists)
                .collect(Collectors.toSet());

        book.setCategory(category);

        book.setPublisher(publisher);

        book.setAuthors(authors);

        Book savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);

    }

    @Override
    public BookResponse updateBook(
            Long id,
            UpdateBookRequest request) {

        Book book = bookValidator.validateBookExists(id);

        String isbn = request.getIsbn().trim();

        bookValidator.validateDuplicateIsbnForUpdate(
                id,
                isbn);

        bookValidator.validateAvailableCopies(
                request.getTotalCopies(),
                request.getAvailableCopies());

        request.setIsbn(isbn);

        Category category = categoryValidator.validateCategoryExists(
                request.getCategoryId());

        Publisher publisher = publisherValidator.validatePublisherExists(
                request.getPublisherId());

        Set<Author> authors = request.getAuthorIds()
                .stream()
                .map(authorValidator::validateAuthorExists)
                .collect(Collectors.toSet());

        bookMapper.updateEntity(
                book,
                request);

        book.setCategory(category);

        book.setPublisher(publisher);

        book.setAuthors(authors);

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toResponse(updatedBook);

    }

    @Override
    public void deleteBook(Long id) {

        Book book = bookValidator.validateBookExists(id);

        if (!book.isActive()) {

            throw new BadRequestException(
                    "Book is already inactive.");

        }

        book.setActive(false);

        bookRepository.save(book);

    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {

        Book book = bookValidator.validateBookExists(id);

        return bookMapper.toResponse(book);

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookResponse> getAllBooks(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Pageable pageable = PageableUtils.createPageable(
                page,
                size,
                sortBy,
                sortDirection);

        Page<Book> books = bookRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
                books,
                bookMapper::toResponse);

    }

}