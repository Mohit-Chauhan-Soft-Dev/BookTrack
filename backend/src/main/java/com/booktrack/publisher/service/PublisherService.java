package com.booktrack.publisher.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.publisher.dto.request.CreatePublisherRequest;
import com.booktrack.publisher.dto.request.UpdatePublisherRequest;
import com.booktrack.publisher.dto.response.PublisherResponse;

public interface PublisherService {

    PublisherResponse createPublisher(CreatePublisherRequest request);

    PublisherResponse updatePublisher(
            Long id,
            UpdatePublisherRequest request);

    void deletePublisher(Long id);

    PublisherResponse getPublisherById(Long id);

    PageResponse<PublisherResponse> getAllPublishers(
            int page,
            int size,
            String sortBy,
            String sortDirection);

}