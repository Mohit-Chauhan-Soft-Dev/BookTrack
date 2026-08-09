package com.booktrack.publisher.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResponse {

    private Long id;

    private String name;

    private String description;

    private String website;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}