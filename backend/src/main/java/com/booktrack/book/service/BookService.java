package com.booktrack.book.service;

import com.booktrack.book.dto.request.CreateBookRequest;
import com.booktrack.book.dto.request.UpdateBookRequest;
import com.booktrack.book.dto.response.BookResponse;
import com.booktrack.common.dto.response.PageResponse;

public interface BookService {

    BookResponse createBook(CreateBookRequest request);

    BookResponse updateBook(
            Long id,
            UpdateBookRequest request);

    void deleteBook(Long id);

    BookResponse getBookById(Long id);

    PageResponse<BookResponse> getAllBooks(
            int page,
            int size,
            String sortBy,
            String sortDirection);

}