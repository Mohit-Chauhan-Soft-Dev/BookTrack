package com.booktrack.fine.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.fine.dto.request.CreateFineRequest;
import com.booktrack.fine.dto.request.PayFineRequest;
import com.booktrack.fine.dto.response.FineResponse;
import org.springframework.security.core.Authentication;

public interface FineService {

        FineResponse createFine(
                        CreateFineRequest request);

        FineResponse payFine(
                        PayFineRequest request,
                        Authentication authentication);

        FineResponse getFineById(
                        Long id,
                        Authentication authentication);

        FineResponse getFineByCirculation(
                        Long circulationId,
                        Authentication authentication);

        PageResponse<FineResponse> getAllFines(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection);

        PageResponse<FineResponse> getFinesByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection,
                        Authentication authentication);
}