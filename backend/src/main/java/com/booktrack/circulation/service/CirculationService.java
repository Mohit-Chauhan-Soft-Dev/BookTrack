package com.booktrack.circulation.service;

import com.booktrack.circulation.dto.request.BorrowBookRequest;
import com.booktrack.circulation.dto.request.ReturnBookRequest;
import com.booktrack.circulation.dto.response.CirculationResponse;
import com.booktrack.common.dto.response.PageResponse;

public interface CirculationService {

    CirculationResponse borrowBook(
            BorrowBookRequest request
    );

    CirculationResponse returnBook(
            ReturnBookRequest request
    );

    CirculationResponse getCirculationById(
            Long id
    );

    PageResponse<CirculationResponse> getAllCirculations(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<CirculationResponse> getCirculationsByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<CirculationResponse> getCirculationsByBookCopy(
            Long bookCopyId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}