package com.booktrack.author.service;

import com.booktrack.author.dto.request.CreateAuthorRequest;
import com.booktrack.author.dto.request.UpdateAuthorRequest;
import com.booktrack.author.dto.response.AuthorResponse;
import com.booktrack.common.dto.response.PageResponse;

public interface AuthorService {

    AuthorResponse createAuthor(CreateAuthorRequest request);

    AuthorResponse updateAuthor(
            Long id,
            UpdateAuthorRequest request
    );

    void deleteAuthor(Long id);

    AuthorResponse getAuthorById(Long id);

    PageResponse<AuthorResponse> getAllAuthors(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

}