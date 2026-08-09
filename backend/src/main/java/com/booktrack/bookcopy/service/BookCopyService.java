package com.booktrack.bookcopy.service;

import com.booktrack.bookcopy.dto.request.CreateBookCopyRequest;
import com.booktrack.bookcopy.dto.request.UpdateBookCopyRequest;
import com.booktrack.bookcopy.dto.response.BookCopyResponse;
import com.booktrack.common.dto.response.PageResponse;

public interface BookCopyService {

    BookCopyResponse createBookCopy(
            CreateBookCopyRequest request);

    BookCopyResponse updateBookCopy(
            Long id,
            UpdateBookCopyRequest request);

    void deleteBookCopy(Long id);

    BookCopyResponse getBookCopyById(Long id);

    PageResponse<BookCopyResponse> getAllBookCopies(
            int page,
            int size,
            String sortBy,
            String sortDirection);

    PageResponse<BookCopyResponse> getBookCopiesByBook(
            Long bookId,
            int page,
            int size,
            String sortBy,
            String sortDirection);
}