package com.booktrack.publisher.mapper;

import com.booktrack.publisher.dto.request.CreatePublisherRequest;
import com.booktrack.publisher.dto.request.UpdatePublisherRequest;
import com.booktrack.publisher.dto.response.PublisherResponse;
import com.booktrack.publisher.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher toEntity(CreatePublisherRequest request) {

        return Publisher.builder()
                .name(request.getName())
                .description(request.getDescription())
                .website(request.getWebsite())
                .build();
    }

    public PublisherResponse toResponse(Publisher publisher) {

        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .description(publisher.getDescription())
                .website(publisher.getWebsite())
                .active(publisher.isActive())
                .createdAt(publisher.getCreatedAt())
                .updatedAt(publisher.getUpdatedAt())
                .build();
    }

    public void updateEntity(
            Publisher publisher,
            UpdatePublisherRequest request) {

        publisher.setName(request.getName());
        publisher.setDescription(request.getDescription());
        publisher.setWebsite(request.getWebsite());
        publisher.setActive(request.isActive());
    }

}