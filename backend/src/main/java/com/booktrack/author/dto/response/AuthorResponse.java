package com.booktrack.author.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String biography;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}