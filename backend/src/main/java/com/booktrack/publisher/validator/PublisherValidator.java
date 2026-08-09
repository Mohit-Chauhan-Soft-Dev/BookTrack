package com.booktrack.publisher.validator;

import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.publisher.entity.Publisher;
import com.booktrack.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublisherValidator {

    private final PublisherRepository publisherRepository;

    public Publisher validatePublisherExists(Long id) {

        return publisherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Publisher not found with id: " + id
                        ));
    }

    public void validateDuplicatePublisher(String name) {

        String publisherName = name.trim();

        if (publisherRepository.existsByNameIgnoreCase(publisherName)) {

            throw new DuplicateResourceException(
                    "Publisher already exists with name: " + publisherName
            );

        }

    }

    public void validateDuplicatePublisherForUpdate(
            Long publisherId,
            String name) {

        String publisherName = name.trim();

        publisherRepository.findByNameIgnoreCase(publisherName)
                .ifPresent(existingPublisher -> {

                    if (!existingPublisher.getId().equals(publisherId)) {

                        throw new DuplicateResourceException(
                                "Publisher already exists with name: "
                                        + publisherName
                        );

                    }

                });

    }

}