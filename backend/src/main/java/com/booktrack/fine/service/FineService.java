package com.booktrack.fine.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.fine.dto.request.CreateFineRequest;
import com.booktrack.fine.dto.request.PayFineRequest;
import com.booktrack.fine.dto.response.FineResponse;


public interface FineService {

    FineResponse createFine(
            CreateFineRequest request
    );

    FineResponse payFine(
            PayFineRequest request
    );

    FineResponse getFineById(
            Long id
    );

    FineResponse getFineByCirculation(
            Long circulationId
    );

    PageResponse<FineResponse> getAllFines(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<FineResponse> getFinesByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}