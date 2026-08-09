package com.booktrack.publisher.controller;

import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.publisher.dto.request.CreatePublisherRequest;
import com.booktrack.publisher.dto.request.UpdatePublisherRequest;
import com.booktrack.publisher.dto.response.PublisherResponse;
import com.booktrack.publisher.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.PUBLISHERS)
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponse> createPublisher(
            @Valid @RequestBody CreatePublisherRequest request) {

        PublisherResponse response =
                publisherService.createPublisher(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                publisherService.getPublisherById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<PublisherResponse>> getAllPublishers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDirection) {

        return ResponseEntity.ok(
                publisherService.getAllPublishers(
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(

            @PathVariable Long id,

            @Valid
            @RequestBody UpdatePublisherRequest request) {

        return ResponseEntity.ok(
                publisherService.updatePublisher(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(
            @PathVariable Long id) {

        publisherService.deletePublisher(id);

        return ResponseEntity.noContent().build();
    }

}