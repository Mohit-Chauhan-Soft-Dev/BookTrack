package com.booktrack.publisher.service.impl;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;
import com.booktrack.publisher.dto.request.CreatePublisherRequest;
import com.booktrack.publisher.dto.request.UpdatePublisherRequest;
import com.booktrack.publisher.dto.response.PublisherResponse;
import com.booktrack.publisher.entity.Publisher;
import com.booktrack.publisher.mapper.PublisherMapper;
import com.booktrack.publisher.repository.PublisherRepository;
import com.booktrack.publisher.service.PublisherService;
import com.booktrack.publisher.validator.PublisherValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    private final PublisherValidator publisherValidator;

    private final PageResponseMapper pageResponseMapper;

    @Override
    public PublisherResponse createPublisher(
            CreatePublisherRequest request) {

        String name = request.getName().trim();

        publisherValidator.validateDuplicatePublisher(name);

        request.setName(name);

        Publisher publisher =
                publisherMapper.toEntity(request);

        Publisher savedPublisher =
                publisherRepository.save(publisher);

        return publisherMapper.toResponse(savedPublisher);
    }

    @Override
    public PublisherResponse updatePublisher(
            Long id,
            UpdatePublisherRequest request) {

        Publisher publisher =
                publisherValidator.validatePublisherExists(id);

        String name = request.getName().trim();

        publisherValidator.validateDuplicatePublisherForUpdate(
                id,
                name
        );

        request.setName(name);

        publisherMapper.updateEntity(
                publisher,
                request
        );

        Publisher updatedPublisher =
                publisherRepository.save(publisher);

        return publisherMapper.toResponse(updatedPublisher);
    }

    @Override
    public void deletePublisher(Long id) {

        Publisher publisher =
                publisherValidator.validatePublisherExists(id);

        if (!publisher.isActive()) {

            throw new BadRequestException(
                    "Publisher is already inactive."
            );

        }

        publisher.setActive(false);

        publisherRepository.save(publisher);
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherResponse getPublisherById(Long id) {

        Publisher publisher =
                publisherValidator.validatePublisherExists(id);

        return publisherMapper.toResponse(publisher);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PublisherResponse> getAllPublishers(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Pageable pageable =
                PageableUtils.createPageable(
                        page,
                        size,
                        sortBy,
                        sortDirection
                );

        Page<Publisher> publisherPage =
                publisherRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
                publisherPage,
                publisherMapper::toResponse
        );
    }

}